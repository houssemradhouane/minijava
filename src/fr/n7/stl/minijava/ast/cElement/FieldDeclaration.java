package fr.n7.stl.minijava.ast.cElement;

import fr.n7.stl.minijava.ast.expression.Expression;
import fr.n7.stl.minijava.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class FieldDeclaration implements ClassElement {
	
	private Type type;
	private String name;
	private int offset;

	public int getOffset() {
		return offset;
	}

	public FieldDeclaration(Type type, String name) {
		this.type = type;
		this.name = name;
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
		return true;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return this.type.resolve(_scope);
	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public int[] allocateMemory(Register _register, int _offset, int instanceOffset) {
		this.offset = _offset;
		int[] ret = {0, this.type.length()};
		return ret;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		return ret;
	}

}
