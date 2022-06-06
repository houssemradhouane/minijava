/**
 * 
 */
package fr.n7.stl.minijava.ast;

import java.util.List;

import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.instruction.Instruction;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.scope.SymbolTable;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Represents a Block node in the Abstract Syntax Tree node for the Bloc language.
 * Declares the various semantics attributes for the node.
 * 
 * A block contains declarations. It is thus a Scope even if a separate SymbolTable is used in
 * the attributed semantics in order to manage declarations.
 * 
 * @author Marc Pantel
 *
 */
public class Block {

	/**
	 * Sequence of instructions contained in a block.
	 */
	protected List<Instruction> instructions;
	
	protected Register register;
	protected int offset;
	protected int taille;
	
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
	
	public int getTaille() {
		return this.taille;
	}

	/**
	 * Constructor for a block.
	 */
	public Block(List<Instruction> _instructions) {
		this.instructions = _instructions;
	}
	
	private HierarchicalScope<Declaration> localScope;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _local = "";
		for (Instruction _instruction : this.instructions) {
			_local += _instruction;
		}
		return "{\n" + _local + "}\n" ;
	}
	
	/**
	 * Inherited Semantics attribute to collect all the identifiers declaration and check
	 * that the declaration are allowed.
	 * @param _scope Inherited Scope attribute that contains the identifiers defined previously
	 * in the context.
	 * @return Synthesized Semantics attribute that indicates if the identifier declaration are
	 * allowed.
	 */
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		this.localScope = new SymbolTable(_scope);
		for(Instruction i : this.instructions) {
			if (!i.collectAndBackwardResolve(localScope))
				return false;
		}
		System.out.println(localScope);
		
		return true;
	}
	
	/**
	 * Inherited Semantics attribute to check that all identifiers have been defined and
	 * associate all identifiers uses with their definitions.
	 * @param _scope Inherited Scope attribute that contains the defined identifiers.
	 * @return Synthesized Semantics attribute that indicates if the identifier used in the
	 * block have been previously defined.
	 */
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		for(Instruction i : this.instructions) {
			if (!i.fullResolve(localScope))
				return false;
		}
		return true;
	}

	/**
	 * Synthesized Semantics attribute to check that an instruction if well typed.
	 * @return Synthesized True if the instruction is well typed, False if not.
	 */	
	public boolean checkType() {
		for(Instruction i : this.instructions) {
			if (!i.checkType())
				return false;
		}
		return true;
	}

	/**
	 * Inherited Semantics attribute to allocate memory for the variables declared in the instruction.
	 * Synthesized Semantics attribute that compute the size of the allocated memory. 
	 * @param _register Inherited Register associated to the address of the variables.
	 * @param _offset Inherited Current offset for the address of the variables.
	 */	
	public void allocateMemory(Register _register, int _offset) {
		int depl = _offset;
		this.taille = 0;
		this.offset = _offset;
		for (Instruction i : this.instructions) {
			depl += i.allocateMemory(_register, depl);
		}
		this.taille = depl - this.offset;
	}

	/**
	 * Inherited Semantics attribute to build the nodes of the abstract syntax tree for the generated TAM code.
	 * Synthesized Semantics attribute that provide the generated TAM code.
	 * @param _factory Inherited Factory to build AST nodes for TAM code.
	 * @return Synthesized AST for the generated TAM code.
	 */
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		for (Instruction i : this.instructions) {
			ret.append(i.getCode(_factory));
		}
		ret.add(_factory.createPop(0, this.taille));
		return ret;
	}
	
	public void setReturnType(Type _type) {
		for (Instruction i : this.instructions) {
			i.setReturnType(_type);
		}
	}

	public void setParamsLength(int _paramOffset) {
		for (Instruction i : this.instructions) {
			i.setParamsLength(_paramOffset);
		}
	}

	public void setInstance(Classe declaration) {
		for (Instruction i : this.instructions) {
			i.setInstance(declaration);
		}
	}

}
