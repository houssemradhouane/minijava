package fr.n7.stl.minijava.ast.cElement;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.minijava.ast.Block;
import fr.n7.stl.minijava.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.scope.SymbolTable;
import fr.n7.stl.minijava.ast.type.AtomicType;
import fr.n7.stl.minijava.ast.type.InstanceType;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ConstructeurDeclaration implements ClassElement {
	
	private static int nb = 0;
	
	private int id;
	private String nom;
	private List<ParameterDeclaration> parametres;
	private Block corps;
	private InstanceType type;
	private int paramOffset;

	public ConstructeurDeclaration(String nom, Block corps, List<ParameterDeclaration> parametres) {
		this.id = ConstructeurDeclaration.nb;
		ConstructeurDeclaration.nb++;
		this.nom = nom;
		this.parametres = parametres;
		this.corps = corps;
	}
	
	public ConstructeurDeclaration(String nom, Block corps) {
		this.nom = nom;
		this.parametres = new LinkedList<ParameterDeclaration>();
		this.corps = corps;
	}
	
	
	public List<ParameterDeclaration> getParametres() {
		return parametres;
	}

	@Override
	public String getName() {
		return this.nom;
	}

	public void setType(InstanceType type) {
		this.type = type;
	}
	
	@Override
	public Type getType() {
		return null;
	}

	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		HierarchicalScope<Declaration> localScope = new SymbolTable(_scope);
		for (ParameterDeclaration p : this.parametres) {
			if (localScope.accepts(p)) {
				localScope.register(p);
			}
			else {
				System.err.println("déclaration de paramètres multiple");
				return false;
			}
		}
		this.corps.setInstance(this.type.getDeclaration());
		return this.corps.collect(localScope);
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean resolvParams = true;
		for (ParameterDeclaration p : this.parametres) {
			resolvParams = resolvParams && p.getType().resolve(_scope);
		}
		return resolvParams && this.corps.resolve(_scope);
	}

	@Override
	public boolean checkType() {
		return this.type.getName().equals(this.getName()) && this.corps.checkType();
	}

	@Override
	public int[] allocateMemory(Register _register, int _offset, int _instanceOffset) {
		int paramOffset = 0;
		
		for (ParameterDeclaration p : this.parametres) {
			paramOffset -= p.getType().length();
		}
		
		this.paramOffset = paramOffset;
		this.corps.setParamsLength(-paramOffset);
		
		for (ParameterDeclaration p : this.parametres) {
			p.setOffset(paramOffset);
			paramOffset += p.getType().length();
		}
		
		this.corps.allocateMemory(Register.LB, 4);
		int[] ret = {0,0};
		return ret;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.add(_factory.createJump("fin_constr_" + this.id));
		ret.addSuffix("debut_constr_" + this.id);
		ret.add(_factory.createLoad(Register.LB, this.paramOffset-1, 1));
		ret.append(this.corps.getCode(_factory));
		ret.add(_factory.createReturn(0, -this.paramOffset));
		ret.addSuffix("fin_constr_" + this.id);
		return ret;
	}

	public int getId() {
		return this.id;
	}

}
