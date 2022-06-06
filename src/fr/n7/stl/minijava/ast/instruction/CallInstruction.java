package fr.n7.stl.minijava.ast.instruction;

import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.expression.FunctionCall;
import fr.n7.stl.minijava.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.AtomicType;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class CallInstruction implements Instruction {
	
	FunctionCall fc;
	
	public CallInstruction(AssignableExpression functionCall) {
		if (functionCall instanceof FunctionCall)
			this.fc = (FunctionCall) functionCall;
		else Logger.error("Tentative d'appel à un objet qui n'est pas une fonction : " + functionCall);
	}

	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return fc.collectAndBackwardResolve(_scope);
	}

	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return fc.fullResolve(_scope);
	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = fc.getCode(_factory);
		if (fc.getType().length() > 0) {
			ret.add(_factory.createPop(0, fc.getType().length()));
		}
		return ret;
	}

	@Override
	public void setReturnType(Type _type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParamsLength(int _paramOffset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInstance(Classe declaration) {
		this.fc.setInstance(declaration);
	}

}
