package fr.n7.stl.minijava.ast.expression;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.ArrayType;
import fr.n7.stl.minijava.ast.type.AtomicType;
import fr.n7.stl.minijava.ast.type.Type;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractArray implements Expression {

	/**
	 * AST node that represents the expression whose result is an array.
	 */
	protected Expression array;
	
	/**
	 * AST node that represents the expression whose result is an integer value used to index the array.
	 */
	protected Expression index;
	
	/**
	 * Construction for the implementation of an array element access expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element access expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element access expression.
	 */
	public AbstractArray(Expression _array, Expression _index) {
		this.array = _array;
		this.index = _index;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.array + "[ " + this.index + " ]");
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#collect(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return this.array.collectAndBackwardResolve(_scope) && this.index.collectAndBackwardResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#resolve(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return this.array.fullResolve(_scope) && this.array.fullResolve(_scope);
	}
	
	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
		if (this.array.getType() instanceof ArrayType) {
			return ((ArrayType) this.array.getType()).getType();
		}
		return AtomicType.ErrorType;
	}

}