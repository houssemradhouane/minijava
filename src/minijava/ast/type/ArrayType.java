/**
 * 
 */
package minijava.ast.type;

import minijava.ast.SemanticsUndefinedException;
import minijava.ast.scope.Declaration;
import minijava.ast.scope.HierarchicalScope;

/**
 * @author Marc Pantel
 *
 */
public class ArrayType implements Type {

	protected Type element;

	public ArrayType(Type _element) {
		this.element = _element;
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Type#equalsTo(minijava.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof ArrayType) {
			return this.element.equalsTo(((ArrayType)_other).element);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Type#compatibleWith(minijava.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if (_other instanceof ArrayType) {
			return this.element.compatibleWith(((ArrayType)_other).element);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Type#merge(minijava.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other instanceof ArrayType) {
			return new ArrayType(this.element.merge(((ArrayType)_other).element));
		} else {
			return AtomicType.ErrorType;
		}
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Type#length(int)
	 */
	@Override
	public int length() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.element + " [])";
	}

	/* (non-Javadoc)
	 * @see minijava.ast.type.Type#resolve(minijava.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return this.element.resolve(_scope);
	}

	/**
	 * @return Type of the elements in the array.
	 */
	public Type getType() {
		return this.element;
	}

}
