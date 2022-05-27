/**
 * 
 */
package fr.n7.stl.minijava.ast.expression.allocation;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.expression.Expression;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.ArrayType;
import fr.n7.stl.minijava.ast.type.AtomicType;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * @author Marc Pantel
 *
 */
public class ArrayAllocation implements Expression {

	protected Type element;
	protected Expression size;
	protected ArrayType type;

	public ArrayAllocation(Type _element, Expression _size) {
		this.element = _element;
		this.size = _size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "new " + this.element + "[ " + this.size + " ]"; 
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return size.collectAndBackwardResolve(_scope);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return size.fullResolve(_scope) && element.resolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		if (!size.getType().compatibleWith(AtomicType.IntegerType))
			return AtomicType.ErrorType;
		if (this.type == null) {
			this.type = new ArrayType(element);
		}
		return this.type;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.append(this.size.getCode(_factory));
		ret.add(_factory.createLoadL(this.element.length()));
		ret.add(Library.IMul);
		ret.add(Library.MAlloc);
		return ret;
	}

}
