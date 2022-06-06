/**
 * 
 */
package fr.n7.stl.minijava.ast.expression.accessible;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.expression.AbstractArray;
import fr.n7.stl.minijava.ast.expression.Expression;
import fr.n7.stl.minijava.ast.type.ArrayType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing an array element.
 * @author Marc Pantel
 *
 */
public class ArrayAccess extends AbstractArray implements AccessibleExpression {

	/**
	 * Construction for the implementation of an array element access expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element access expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element access expression.
	 */
	public ArrayAccess(Expression _array, Expression _index) {
		super(_array,_index);
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Expression#getCode(tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.append(this.array.getCode(_factory));
		if (this.array instanceof AccessibleExpression) {
			ret.add(_factory.createLoadI(this.array.getType().length()));
		}
		ret.append(this.index.getCode(_factory));
		ret.add(_factory.createLoadL(((ArrayType) this.array.getType()).getType().length()));
		ret.add(Library.IMul);
		ret.add(Library.IAdd);
		return ret;
	}

	@Override
	public void setInstance(Classe declaration) {
		this.array.setInstance(declaration);
		this.index.setInstance(declaration);
	}

}
