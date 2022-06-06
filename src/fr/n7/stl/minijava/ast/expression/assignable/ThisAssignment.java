package fr.n7.stl.minijava.ast.expression.assignable;

import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ThisAssignment implements AssignableExpression {
	
	private Classe classe;

	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return this.classe != null;
	}

	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}
	
	public Classe getClasse() {
		return classe;
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.add(_factory.createLoad(Register.LB, 3, 1));
		return ret;
	}

	@Override
	public void setInstance(Classe declaration) {
		this.classe = declaration;
	}
	

}
