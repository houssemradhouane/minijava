package fr.n7.stl.minijava.ast.expression;

/**
 * @author Marc Pantel
 *
 */
public enum UnaryOperator {
	
	/**
	 * Boolean negation
	 */
	Negate,
	/**
	 * Numeric opposite
	 */
	Opposite;

	@Override
	public String toString() {
		switch (this) {
		case Negate: return "!";
		case Opposite: return "-";
		default: throw new IllegalArgumentException( "Unary Operator not defined.");		
		}
	}

}
