/**
 * 
 */
package fr.n7.stl.minijava.ast.expression;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.AtomicType;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a conditional expression.
 * @author Marc Pantel
 *
 */
public class ConditionalExpression implements Expression {
	
	static int nb = 0;

	/**
	 * AST node for the expression whose value is the condition for the conditional expression.
	 */
	protected Expression condition;
	
	/**
	 * AST node for the expression whose value is the then parameter for the conditional expression.
	 */
	protected Expression thenExpression;
	
	/**
	 * AST node for the expression whose value is the else parameter for the conditional expression.
	 */
	protected Expression elseExpression;
	
	/**
	 * Builds a binary expression Abstract Syntax Tree node from the left and right sub-expressions
	 * and the binary operation.
	 * @param _left : Expression for the left parameter.
	 * @param _operator : Binary Operator.
	 * @param _right : Expression for the right parameter.
	 */
	public ConditionalExpression(Expression _condition, Expression _then, Expression _else) {
		this.condition = _condition;
		this.thenExpression = _then;
		this.elseExpression = _else;
	}

	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#collect(minijava.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return this.condition.collectAndBackwardResolve(_scope) &&
				this.thenExpression.collectAndBackwardResolve(_scope) &&
				this.elseExpression.collectAndBackwardResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#resolve(minijava.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return this.condition.fullResolve(_scope) &&
				this.thenExpression.fullResolve(_scope) &&
				this.elseExpression.fullResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.condition + " ? " + this.thenExpression + " : " + this.elseExpression + ")";
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		Type result = AtomicType.ErrorType;
		if (this.condition.getType().compatibleWith(AtomicType.BooleanType)){
			if (this.thenExpression.getType().equalsTo(this.elseExpression.getType())) {
				result = this.thenExpression.getType().merge(this.elseExpression.getType());
			} else {
				System.err.println("erreur de typage " + this +" then et else doivent avoir le même type");
			}
		} else {
			System.err.println("erreur de typage " + this +" la condition n'est pas un booleen");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Expression#getCode(tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.append(this.condition.getCode(_factory));
		if (this.condition instanceof AccessibleExpression) {
			ret.add(_factory.createLoadI(this.condition.getType().length()));
		}
		ret.add(_factory.createJumpIf("cond_exp_else_" + ConditionalExpression.nb, 0));
		ret.append(this.thenExpression.getCode(_factory));
		if (this.thenExpression instanceof AccessibleExpression) {
			ret.add(_factory.createLoadI(this.thenExpression.getType().length()));
		}
		ret.add(_factory.createJump("cond_exp_end_" + ConditionalExpression.nb));
		ret.addSuffix("cond_exp_else_" + ConditionalExpression.nb);
		ret.append(this.elseExpression.getCode(_factory));
		if (this.elseExpression instanceof AccessibleExpression) {
			ret.add(_factory.createLoadI(this.elseExpression.getType().length()));
		}
		ret.addSuffix("cond_exp_end_" + ConditionalExpression.nb);
		ConditionalExpression.nb++;
		return ret;
	}

}
