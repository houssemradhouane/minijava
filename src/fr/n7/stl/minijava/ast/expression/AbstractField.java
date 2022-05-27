package fr.n7.stl.minijava.ast.expression;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.expression.accessible.FieldAccess;
import fr.n7.stl.minijava.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.minijava.ast.expression.assignable.FieldAssignment;
import fr.n7.stl.minijava.ast.expression.assignable.VariableAssignment;
import fr.n7.stl.minijava.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.FieldDeclaration;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractField implements Expression {

	protected Expression expr;
	protected String name;
	protected Declaration field;
	
	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public AbstractField(Expression _expr, String _name) {
		this.expr = _expr;
		this.name = _name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.expr + "." + this.name;
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#collect(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return expr.collectAndBackwardResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#resolve(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		if (this.expr instanceof IdentifierAccess || this.expr instanceof VariableAssignment) {
			AbstractIdentifier _local = (AbstractIdentifier) this.expr;
			Declaration _decl = _scope.get(_local.name);
			if (_decl instanceof VariableDeclaration) {
				Type T = ((VariableDeclaration) _decl).getType();
				throw new SemanticsUndefinedException( "Semantics collect is undefined in AbstractField.");
				/*if (T instanceof RecordType) {
					RecordType _recT = (RecordType) T;
					if (_recT.contains(this.name)) {
						this.field = _recT.get(this.name);
						return this.record.fullResolve(_scope);
					}
				}*/
			} else if (_decl instanceof Classe) {
				// Variable ou Méthode de classe
				Classe cl = (Classe) _decl;
				if (cl.contains(this.name)) {
					this.field = cl.get(this.name);
					return this.expr.fullResolve(_scope);
				}
			}
		}
		if (this.expr instanceof FieldAccess || this.expr instanceof FieldAssignment) {
			AbstractField _local = (AbstractField) this.expr;
			if (!_local.fullResolve(_scope))
				return false;
			Type T = _local.field.getType();
			throw new SemanticsUndefinedException( "Semantics collect is undefined in AbstractField.");
			/*if (T instanceof RecordType) {
				RecordType _recT = (RecordType) T;
				if (_recT.contains(this.name)) {
					this.field = _recT.get(this.name);
					return this.record.fullResolve(_scope);
				}
			}*/
		}
		System.err.println("Champ non défini : " + this);
		return false;
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in AbstractField.");
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
		return this.field.getType();
	}

}