package fr.n7.stl.minijava.ast.expression.accessible;

import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.expression.AbstractConversion;
import fr.n7.stl.minijava.ast.expression.Expression;
import fr.n7.stl.minijava.ast.type.Type;

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

	@Override
	public void setInstance(Classe declaration) {
		this.target.setInstance(declaration);
	}

}
