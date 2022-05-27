/**
 * 
 */
package fr.n7.stl.minijava.ast.expression.accessible;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.expression.AbstractField;
import fr.n7.stl.minijava.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAccess extends AbstractField implements AccessibleExpression {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public FieldAccess(Expression _expr, String _name) {
		super(_expr, _name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		/*Fragment ret = _factory.createFragment();
		ret.append(this.expr.getCode(_factory));
		ret.add(_factory.createLoadL(this.field.getOffset()));
		ret.add(Library.IAdd);
		//ret.add(_factory.createLoadI(this.getType().length()));
		return ret;*/
		throw new SemanticsUndefinedException("getCode not defined in FieldAccess");
	}

}
