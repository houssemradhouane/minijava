package minijava.ast.scope;

import minijava.ast.type.Type;

/**
 * @author Marc Pantel
 *
 */
public interface Declaration {
	
	/**
	 * Provide the identifier (i.e. name) given to the declaration.
	 * @return Name of the declaration.
	 */
	public String getName();

	/**
	 * Provide the type given to the declaration.
	 * @return Type of the declaration.
	 */
	public Type getType();

}