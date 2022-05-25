/**
 * 
 */
package minijava.ast.expression;

import minijava.ast.SemanticsUndefinedException;
import minijava.ast.scope.Declaration;
import minijava.ast.scope.HierarchicalScope;
import minijava.ast.type.Type;
import tam.ast.Fragment;
import tam.ast.TAMFactory;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractConversion<TargetType> implements Expression {

	protected TargetType target;
	protected Type type;
	protected String name;

	public AbstractConversion(TargetType _target, String _type) {
		this.target = _target;
		this.name = _type;
		this.type = null;
	}
	
	public AbstractConversion(TargetType _target, Type _type) {
		this.target = _target;
		this.name = null;
		this.type = _type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (this.type == null) {
			return "(" + this.name + ") " + this.target;
		} else {
			return "(" + this.type + ") " + this.target;
		}
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		throw new SemanticsUndefinedException("Semantics getType undefined in TypeConversion.");
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#collect(minijava.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		throw new SemanticsUndefinedException("Semantics collect undefined in TypeConversion.");
	}

	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#resolve(minijava.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		throw new SemanticsUndefinedException("Semantics resolve undefined in TypeConversion.");
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Expression#getCode(tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		throw new SemanticsUndefinedException("Semantics getCode undefined in TypeConversion.");
	}

}
