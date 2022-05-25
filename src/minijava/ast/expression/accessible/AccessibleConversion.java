package minijava.ast.expression.accessible;

import minijava.ast.expression.AbstractConversion;
import minijava.ast.expression.Expression;
import minijava.ast.type.Type;

/**
 * Implementation of the Abstract Syntax Tree node for a type conversion expression.
 * @author Marc Pantel
 *
 */
public class AccessibleConversion extends AbstractConversion<Expression> implements AccessibleExpression {

	public AccessibleConversion(Expression _target, String _type) {
		super(_target, _type);
	}

	public AccessibleConversion(Expression _target, Type _type) {
		super(_target, _type);
	}

}
