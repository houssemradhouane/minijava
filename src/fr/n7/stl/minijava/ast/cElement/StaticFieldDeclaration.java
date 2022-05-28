package fr.n7.stl.minijava.ast.cElement;

import fr.n7.stl.minijava.ast.expression.Expression;
import fr.n7.stl.minijava.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class StaticFieldDeclaration implements ClassElement {
	
	private Type type;
	private String name;
	private Expression valeurInitiale;
	private Register register;
	private int offset;
	
	public Register getRegister() {
		return register;
	}

	public int getOffset() {
		return offset;
	}

	public StaticFieldDeclaration(Type type, String name, Expression valeur) {
		this.type = type;
		this.name = name;
		this.valeurInitiale = valeur;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		return this.valeurInitiale.collectAndBackwardResolve(_scope);
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return this.type.resolve(_scope) && this.valeurInitiale.fullResolve(_scope);
	}

	@Override
	public boolean checkType() {
		Type other = this.getType();
		boolean result = this.valeurInitiale.getType().compatibleWith(other);
		if (!result) {
			System.err.println("erreur de typage " + this);
		}
		return result;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.register = _register;
		this.offset = _offset;
		return this.type.length();
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.add(_factory.createPush(this.type.length()));
		ret.append(valeurInitiale.getCode(_factory));
		if (this.valeurInitiale instanceof AccessibleExpression) {
			ret.add(_factory.createLoadI(this.valeurInitiale.getType().length()));
		}
		ret.add(_factory.createStore(this.register, this.offset, this.type.length()));
		return ret;
	}

}
