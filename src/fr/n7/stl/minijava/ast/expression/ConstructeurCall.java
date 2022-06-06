package fr.n7.stl.minijava.ast.expression;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.minijava.ast.cElement.ClassElement;
import fr.n7.stl.minijava.ast.cElement.ConstructeurDeclaration;
import fr.n7.stl.minijava.ast.cElement.StaticMethodDeclaration;
import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minijava.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minijava.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.InstanceType;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ConstructeurCall implements Expression, AssignableExpression {
	
	private Classe classe;
	private ConstructeurDeclaration cd;
	private Type type;
	private List<Expression> parametres;

	public ConstructeurCall(Type type, List<Expression> parametres) {
		this.type = type;
		this.parametres = parametres;
		this.cd = null;
	}
	
	public ConstructeurCall(Type type) {
		this.type = type;
		this.parametres = new LinkedList<Expression>();
		this.cd = null;
	}

	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean ret = true;
		for (Expression a : this.parametres) {
			ret = ret && a.collectAndBackwardResolve(_scope);
		}
		return ret;
	}

	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean ret = true;
		for (Expression a : this.parametres) {
			ret = ret && a.fullResolve(_scope);
		}
		if (!ret) return false;
		if (this.type.resolve(_scope) && this.type instanceof InstanceType) {
			InstanceType iType = (InstanceType) this.type;
			Classe c = iType.getDeclaration();
			if (c.contains(iType.getName())) {
				ClassElement ce = c.get(iType.getName());
				if (ce instanceof ConstructeurDeclaration) {
					this.cd = (ConstructeurDeclaration) ce;
					this.classe = c;
					if (this.cd.getParametres().size() == this.parametres.size()) {
						for (int i = 0; i < this.parametres.size(); i++) {
							if (!this.parametres.get(i).getType().compatibleWith(this.cd.getParametres().get(i).getType())) {
								System.err.println("resolve error : the function call does not exists." + this);
								return false;
							}
						}
						return true;
					} else {
						System.err.println("resolve error : the function call does not exists." + this);
						return false;
					}
				}
			}
		}
		System.err.println("resolve error : the function call does not exists.");
		return false;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.add(_factory.createLoadL(this.classe.getInstanceLength()));
		ret.add(Library.MAlloc);
		
		for (Expression a : this.parametres) {
			ret.append(a.getCode(_factory));
			if (a instanceof AccessibleExpression) {
				ret.add(_factory.createLoadI(a.getType().length()));
			}
		}
		ret.add(_factory.createCall("debut_constr_" + this.cd.getId(), Register.LB));
		return ret;
	}

	@Override
	public void setInstance(Classe declaration) {
		for (Expression e : this.parametres) {
			e.setInstance(declaration);
		}
	}

}
