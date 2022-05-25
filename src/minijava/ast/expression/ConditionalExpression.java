/**
 * 
 */
package minijava.ast.expression;

import minijava.ast.SemanticsUndefinedException;
import minijava.ast.scope.Declaration;
import minijava.ast.scope.HierarchicalScope;
import minijava.ast.type.AtomicType;
import minijava.ast.type.Type;
import tam.ast.Fragment;
import tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a conditional expression.
 * @author Marc Pantel
 *
 */
public class ConditionalExpression implements Expression {

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
		throw new SemanticsUndefinedException( "Semantics getCode is undefined in ConditionalExpression.");
	}

}
