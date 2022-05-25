/**
 * 
 */
package minijava.ast.instruction.declaration;

import minijava.ast.SemanticsUndefinedException;
import minijava.ast.expression.Expression;
import minijava.ast.expression.accessible.AccessibleExpression;
import minijava.ast.instruction.Instruction;
import minijava.ast.scope.Declaration;
import minijava.ast.scope.HierarchicalScope;
import minijava.ast.type.Type;
import tam.ast.Fragment;
import tam.ast.Register;
import tam.ast.TAMFactory;
import tam.ast.impl.FragmentImpl;

/**
 * Abstract Syntax Tree node for a variable declaration instruction.
 * @author Marc Pantel
 *
 */
public class VariableDeclaration implements Declaration, Instruction {

	/**
	 * Name of the declared variable.
	 */
	protected String name;
	
	/**
	 * AST node for the type of the declared variable.
	 */
	protected Type type;
	
	/**
	 * AST node for the initial value of the declared variable.
	 */
	protected Expression value;
	
	/**
	 * Address register that contains the base address used to store the declared variable.
	 */
	protected Register register;
	
	/**
	 * Offset from the base address used to store the declared variable
	 * i.e. the size of the memory allocated to the previous declared variables
	 */
	protected int offset;
	
	/**
	 * Creates a variable declaration instruction node for the Abstract Syntax Tree.
	 * @param _name Name of the declared variable.
	 * @param _type AST node for the type of the declared variable.
	 * @param _value AST node for the initial value of the declared variable.
	 */
	public VariableDeclaration(String _name, Type _type, Expression _value) {
		this.name = _name;
		this.type = _type;
		this.value = _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.type + " " + this.name + " = " + this.value + ";\n";
	}

	/**
	 * Synthesized semantics attribute for the type of the declared variable.
	 * @return Type of the declared variable.
	 */
	public Type getType() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see fr.n7.minijava.ast.VariableDeclaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Synthesized semantics attribute for the register used to compute the address of the variable.
	 * @return Register used to compute the address where the declared variable will be stored.
	 */
	public Register getRegister() {
		return this.register;
	}
	
	/**
	 * Synthesized semantics attribute for the offset used to compute the address of the variable.
	 * @return Offset used to compute the address where the declared variable will be stored.
	 */
	public int getOffset() {
		return this.offset;
	}
	
	/* (non-Javadoc)
	 * @see minijava.ast.instruction.Instruction#collect(minijava.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		if (_scope.accepts(this)) {
			if (this.value.collectAndBackwardResolve(_scope)) {
				_scope.register(this);
				return true;
			}
			return false;
		} else {
			System.err.println("déclaration multiple");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see minijava.ast.instruction.Instruction#resolve(minijava.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return this.value.fullResolve(_scope) && this.type.resolve(_scope);
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		Type other = this.getType();
		boolean result = this.value.getType().compatibleWith(other);
		if (!result) {
			System.err.println("erreur de typage " + this);
			System.err.println(other);
			System.err.println(this.value.getType());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Instruction#allocateMemory(tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.register = _register;
		this.offset = _offset;
		return this.type.length();
	}

	/* (non-Javadoc)
	 * @see minijava.ast.Instruction#getCode(tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.add(_factory.createPush(this.type.length()));
		ret.append(value.getCode(_factory));
		if (this.value instanceof AccessibleExpression) {
			ret.add(_factory.createLoadI(this.value.getType().length()));
		}
		ret.add(_factory.createStore(this.register, this.offset, this.type.length()));
		return ret;
	}

	@Override
	public void setReturnType(Type _type) {
	}

}
