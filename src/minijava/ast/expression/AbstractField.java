package minijava.ast.expression;

import minijava.ast.SemanticsUndefinedException;
import minijava.ast.expression.accessible.FieldAccess;
import minijava.ast.expression.accessible.IdentifierAccess;
import minijava.ast.expression.assignable.FieldAssignment;
import minijava.ast.expression.assignable.VariableAssignment;
import minijava.ast.instruction.declaration.VariableDeclaration;
import minijava.ast.scope.Declaration;
import minijava.ast.scope.HierarchicalScope;
import minijava.ast.type.NamedType;
import minijava.ast.type.RecordType;
import minijava.ast.type.Type;
import minijava.ast.type.declaration.FieldDeclaration;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractField implements Expression {

	protected Expression record;
	protected String name;
	protected FieldDeclaration field;
	
	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public AbstractField(Expression _record, String _name) {
		this.record = _record;
		this.name = _name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.record + "." + this.name;
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#collect(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return record.collectAndBackwardResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see minijava.ast.expression.Expression#resolve(minijava.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		if (record instanceof IdentifierAccess || record instanceof VariableAssignment) {
			AbstractIdentifier _local = (AbstractIdentifier) record;
			Declaration _decl = _scope.get(_local.name);
			if (_decl instanceof VariableDeclaration) {
				Type T = ((VariableDeclaration) _decl).getType();
				while (T instanceof NamedType) {
					T = ((NamedType) T).getType();
				}
				if (T instanceof RecordType) {
					RecordType _recT = (RecordType) T;
					if (_recT.contains(this.name)) {
						this.field = _recT.get(this.name);
						return this.record.fullResolve(_scope);
					}
				}
			}
		}
		if (record instanceof FieldAccess || record instanceof FieldAssignment) {
			AbstractField _local = (AbstractField) record;
			if (!_local.fullResolve(_scope))
				return false;
			Type T = _local.field.getType();
			while (T instanceof NamedType) {
				T = ((NamedType) T).getType();
			}
			if (T instanceof RecordType) {
				RecordType _recT = (RecordType) T;
				if (_recT.contains(this.name)) {
					this.field = _recT.get(this.name);
					return this.record.fullResolve(_scope);
				}
			}
		}
		System.err.println("Champ non défini : " + this);
		return false;
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
		return this.field.getType();
	}

}