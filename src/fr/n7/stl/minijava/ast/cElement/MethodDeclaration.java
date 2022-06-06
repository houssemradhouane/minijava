package fr.n7.stl.minijava.ast.cElement;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.minijava.ast.scope.SymbolTable;
import fr.n7.stl.minijava.ast.Block;
import fr.n7.stl.minijava.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.AtomicType;
import fr.n7.stl.minijava.ast.type.InstanceType;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class MethodDeclaration implements Function {
	
	private static int nb = 0;
	
	private int id;
	private Type returnType;
	private InstanceType instType;
	private String name;
	private List<ParameterDeclaration> parametres;
	private Block body;
	private int paramOffset;

	public MethodDeclaration(Signature sig, Block body) {
		this.id = MethodDeclaration.nb;
		MethodDeclaration.nb++;
		this.returnType = sig.getType();
		this.name = sig.getNom();
		this.parametres = sig.getParametres();
		if (this.parametres == null) this.parametres = new LinkedList<ParameterDeclaration>();
		this.body = body;
	}
	
	public List<ParameterDeclaration> getParametres() {
		return this.parametres;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		return this.returnType;
	}
	
	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		/*HierarchicalScope<Declaration> localScope;
		if (_scope.accepts(this)) {
			_scope.register(this);
			localScope = new SymbolTable(_scope);
			for (ParameterDeclaration p : this.parametres) {
				if (localScope.accepts(p)) {
					localScope.register(p);
				}
				else {
					System.err.println("déclaration de paramètres multiple");
					return false;
				}
			}
			return body.collect(localScope);
		} else {
			System.err.println("Déclaration de fonction multiple");
			return false;
		}*/
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
		this.body.setInstance(this.instType.getDeclaration());
		return body.collect(localScope);
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean resolvParams = true;
		for (ParameterDeclaration p : this.parametres) {
			resolvParams = resolvParams && p.getType().resolve(_scope);
		}
		return resolvParams && this.returnType.resolve(_scope) && this.body.resolve(_scope);
	}

	@Override
	public boolean checkType() {
		this.body.setReturnType(this.returnType);
		return this.body.checkType();
	}

	@Override
	public int[] allocateMemory(Register _register, int _offset, int _instanceOffset) {
		int paramOffset = 0;
		
		for (ParameterDeclaration p : this.parametres) {
			paramOffset -= p.getType().length();
		}
		
		this.paramOffset = paramOffset;
		this.body.setParamsLength(-paramOffset);
		
		for (ParameterDeclaration p : this.parametres) {
			p.setOffset(paramOffset);
			paramOffset += p.getType().length();
		}
		
		this.body.allocateMemory(Register.LB, 3);
		int[] ret = {0,0};
		return ret;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.add(_factory.createJump("fin_fct_" + this.id));
		ret.addSuffix("debut_fct_" + this.id);
		ret.add(_factory.createLoad(Register.LB, this.paramOffset-1, 1));
		ret.append(this.body.getCode(_factory));
		if (this.returnType.compatibleWith(AtomicType.VoidType))
			ret.add(_factory.createReturn(0, -this.paramOffset));
		ret.addSuffix("fin_fct_" + this.id);
		return ret;
	}
	
	public int getId() {
		return this.id;
	}

	public void setInstanceType(InstanceType instanceType) {
		this.instType = instanceType;
	}

}
