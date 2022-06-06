/**
 * 
 */
package fr.n7.stl.minijava.ast.type;

import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a named type.
 * 
 * @author Marc Pantel
 *
 */
public class InstanceType implements Type {

	private Classe declaration;

	private String name;

	public InstanceType(String _name) {
		this.name = _name;
		this.declaration = null;
	}
	
	public InstanceType(Classe decl) {
		this.name = decl.getName();
		this.declaration = decl;
	}

	
	public Classe getDeclaration() {
		return declaration;
	}

	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof InstanceType) {
			return (this.declaration.getName().equals(((InstanceType) _other).declaration.getName()));
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if (_other instanceof InstanceType) {
			return (this.declaration.getName().equals(((InstanceType) _other).declaration.getName()));
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other instanceof InstanceType) {
			if (this.declaration.getName().equals(((InstanceType) _other).declaration.getName())) {
				return this;
			} else {
				return AtomicType.ErrorType;
			}
		} else {
			return AtomicType.ErrorType;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		//return this.declaration.instanceLength();
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		if (this.declaration == null) {
			if (_scope.knows(this.name)) {
				try {
					Classe _declaration = (Classe) _scope.get(this.name);
					this.declaration = _declaration;
					return true;
				} catch (ClassCastException e) {
					Logger.error("The declaration for " + this.name + " is not a Class.");
					return false;
				}
			} else {
				Logger.error("The identifier " + this.name + " has not been found.");
				return false;
			}
		} else {
			return true;
		}
	}

}
