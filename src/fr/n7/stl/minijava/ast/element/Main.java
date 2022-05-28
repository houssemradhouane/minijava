package fr.n7.stl.minijava.ast.element;

import fr.n7.stl.minijava.ast.Block;
import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class Main implements Element {
	
	protected Block body;
	
	public Main(Block bloc) {
		this.body = bloc;
	}

	public boolean collect(HierarchicalScope<Declaration> _scope) {
		return this.body.collect(_scope);
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return this.body.resolve(_scope);
	}
	
	public boolean checkType() {
		return this.body.checkType();
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.body.allocateMemory(_register, _offset);
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.append(this.body.getCode(_factory));
		return ret;
	}
	
}
