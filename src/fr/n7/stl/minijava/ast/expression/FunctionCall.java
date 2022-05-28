/**
 * 
 */
package fr.n7.stl.minijava.ast.expression;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.cElement.StaticMethodDeclaration;
import fr.n7.stl.minijava.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minijava.ast.expression.accessible.FieldAccess;
import fr.n7.stl.minijava.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minijava.ast.expression.assignable.FieldAssignment;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a function call expression.
 * @author Marc Pantel
 *
 */
public class FunctionCall implements Expression, AssignableExpression {

	/**
	 * Name of the called function.
	 * TODO : Should be an expression.
	 */
	protected Expression expr;
	
	/**
	 * Declaration of the called function after name resolution.
	 * TODO : Should rely on the VariableUse class.
	 */
	protected Declaration function;
	
	/**
	 * List of AST nodes that computes the values of the parameters for the function call.
	 */
	protected List<Expression> arguments;
	
	/**
	 * @param _name : Name of the called function.
	 * @param _arguments : List of AST nodes that computes the values of the parameters for the function call.
	 */
	public FunctionCall(Expression _expr, List<Expression> _arguments) {
		this.expr = _expr;
		this.function = null;
		this.arguments = _arguments;
	}
	
	public FunctionCall(Expression _expr) {
		this.expr = _expr;
		this.function = null;
		this.arguments = new LinkedList<Expression>();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = ((this.function == null)?this.expr:this.function) + "( ";
		Iterator<Expression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
		return  _result + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean ret = true;
		for (Expression a : this.arguments) {
			ret = ret && a.collectAndBackwardResolve(_scope);
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean ret = true;
		for (Expression a : this.arguments) {
			ret = ret && a.fullResolve(_scope);
		}
		if (!ret) return false;
		ret = ret && this.expr.fullResolve(_scope);
		if (this.expr instanceof FieldAccess || this.expr instanceof FieldAssignment) {
			this.function = ((AbstractField) this.expr).field;
			return ret;
		} else {
			System.err.println("resolve error : the function call does not exists.");
			return false;
		}
		/*if (_scope.knows(this.name) && _scope.get(this.name) instanceof FunctionDeclaration) {
			this.function = (FunctionDeclaration) _scope.get(this.name);
			if (this.function.getParameters().size() == this.arguments.size()) {
				for (int i = 0; i < this.arguments.size(); i++) {
					if (!this.arguments.get(i).getType().compatibleWith(this.function.getParameters().get(i).getType())) {
						System.err.println("resolve error : the function call does not exists.");
						return false;
					}
				}
				return true;
			} else {
				System.err.println("resolve error : the function call does not exists.");
				return false;
			}
		} else {
			System.err.println("resolve error : the function call does not exists.");
			return false;
		}*/
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return this.function.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		for (Expression a : this.arguments) {
			ret.append(a.getCode(_factory));
			if (a instanceof AccessibleExpression) {
				ret.add(_factory.createLoadI(a.getType().length()));
			}
		}
		if (function instanceof StaticMethodDeclaration)
			ret.add(_factory.createCall("debut_static_fct_" + ((StaticMethodDeclaration) this.function).getId(), Register.LB));
		return ret;
	}

}
