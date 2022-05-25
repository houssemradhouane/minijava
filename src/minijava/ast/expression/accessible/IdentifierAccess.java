/**
 * 
 */
package minijava.ast.expression.accessible;

import minijava.ast.expression.AbstractIdentifier;
import minijava.ast.SemanticsUndefinedException;
import minijava.ast.expression.AbstractAccess;
import minijava.ast.instruction.declaration.ParameterDeclaration;
import minijava.ast.instruction.declaration.VariableDeclaration;
import minijava.ast.scope.Declaration;
import minijava.ast.scope.HierarchicalScope;
import minijava.ast.type.Type;
import tam.ast.Fragment;
import tam.ast.TAMFactory;
import util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for an identifier access expression (can be a variable,
 * a parameter, a constant, a function, ...).
 * Will be connected to an appropriate object after resolving the identifier to know to which kind of element
 * it is associated (variable, parameter, constant, function, ...).
 * @author Marc Pantel
 * TODO : Should also hold a function and not only a variable.
 */
public class IdentifierAccess extends AbstractIdentifier implements AccessibleExpression {
	
	protected AbstractAccess expression;
	
	/**
	 * Creates a variable use expression Abstract Syntax Tree node.
	 * @param _name Name of the used variable.
	 */
	public IdentifierAccess(String _name) {
		super(_name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override 
	public String toString() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#collect(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		/* This is the backward resolve part. */
		if (((HierarchicalScope<Declaration>)_scope).knows(this.name)) {
			Declaration _declaration = _scope.get(this.name);
			/* These kinds are handled by partial resolve. */
			if (_declaration instanceof VariableDeclaration) {
				this.expression = new VariableAccess((VariableDeclaration) _declaration);
			} else if (_declaration instanceof ParameterDeclaration) {
				this.expression = new ParameterAccess((ParameterDeclaration) _declaration);
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#resolve(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		/* This is the full resolve part that complements the backward resolve. */
		/* If the resolution has not been done by the backward resolve */
		if  (this.expression == null) {
			if (((HierarchicalScope<Declaration>)_scope).knows(this.name)) {
				Declaration _declaration = _scope.get(this.name);
				/* This kind should have been handled by partial resolve. */
				if (_declaration instanceof VariableDeclaration || _declaration instanceof ParameterDeclaration) {
					throw new SemanticsUndefinedException( "Collect and partial resolve have probably not been implemented correctly. The identifier " + this.name + " should have not been resolved previously.");
				}
			} else {
				Logger.error("The identifier " + this.name + " has not been found.");
				return false;	
			}
		} else {  /* The resolution has been done previously */
			return true;
		}
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		if (this.expression != null) {
			return this.expression.getType();
		} else {
			throw new SemanticsUndefinedException( "Collect and Resolve have probably not been implemented correctly. The identifier " + this.name + " has not been resolved.");
		}
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Expression#getCode(tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		return this.expression.getCode(_factory);
	}

}
