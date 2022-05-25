/**
 * 
 */
package minijava.ast.type;

import minijava.ast.scope.Declaration;
import minijava.ast.scope.HierarchicalScope;

/**
 * Elementary types in the Bloc language.
 * @author Marc Pantel
 *
 */
public enum AtomicType implements Type {
	BooleanType,
	CharacterType,
	FloatingType,
	IntegerType,
	StringType,
	VoidType,
	NullType,
	ErrorType
	;

	/* (non-Javadoc)
	 * @see block.ast.Type#equalsTo(block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		return this == _other;
	}

	/* (non-Javadoc)
	 * @see block.ast.Type#compatibleWith(block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		if (this.equalsTo(_other)) {
			return true;
		} else {
			switch (this) {
			case NullType : return ((_other != ErrorType) && (_other != VoidType));
			case IntegerType: return (_other == FloatingType);
			default: return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see block.ast.Type#merge(block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (this.compatibleWith(_other)) {
			return _other;
		} else {
			if (_other.compatibleWith(this)) {
				return this;
			} else {
				return ErrorType;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see block.ast.Type#length()
	 */
	@Override
	public int length() {
		switch (this) {
			case NullType : 
			case BooleanType :
			case CharacterType :
			case FloatingType :
			case IntegerType :
			case StringType : return 1;
			case VoidType : return 0;
			default : throw new IllegalArgumentException( "Must not call length on the Error type.");
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		switch (this) {
		case BooleanType: return "boolean";
		case CharacterType: return "char";
		case ErrorType: return "error";
		case FloatingType: return "float";
		case IntegerType: return "int";
		case StringType: return "String";
		case VoidType: return "void";
		case NullType: return "unit";
		default: throw new IllegalArgumentException( "The default case should never be triggered.");
		}
	}

	/* (non-Javadoc)
	 * @see minijava.ast.type.Type#resolve(block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}

}
