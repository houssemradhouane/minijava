/**
 * 
 */
package fr.n7.stl.minijava.ast.instruction;

import java.util.Optional;

import fr.n7.stl.minijava.ast.Block;
import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.expression.Expression;
import fr.n7.stl.minijava.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.AtomicType;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Conditional implements Instruction {
	
	private static int id = 0;

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return this.condition.collectAndBackwardResolve(_scope) &&
				this.thenBranch.collect(_scope) &&
				(this.elseBranch == null || this.elseBranch.collect(_scope));
		
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return this.condition.fullResolve(_scope) &&
				this.thenBranch.resolve(_scope) &&
				(this.elseBranch == null || this.elseBranch.resolve(_scope));
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		return this.condition.getType().compatibleWith(AtomicType.BooleanType) &&
				this.thenBranch.checkType() && (this.elseBranch == null || this.elseBranch.checkType());
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.thenBranch.allocateMemory(_register, _offset);
		if (this.elseBranch != null) {
			this.elseBranch.allocateMemory(_register, _offset);
		}
		return 0;
		
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		ret.append(this.condition.getCode(_factory));
		if (this.condition instanceof AccessibleExpression) {
			ret.add(_factory.createLoadI(this.condition.getType().length()));
		}
		ret.add(_factory.createJumpIf("cond_else_" + Conditional.id, 0));
		ret.append(this.thenBranch.getCode(_factory));
		ret.add(_factory.createJump("cond_end_" + Conditional.id));
		ret.addSuffix("cond_else_" + Conditional.id);
		if (this.elseBranch != null) {
			ret.append(this.elseBranch.getCode(_factory));
		}
		ret.addSuffix("cond_end_" + Conditional.id);
		Conditional.id++;
		return ret;
		
	}

	@Override
	public void setReturnType(Type _type) {
		this.thenBranch.setReturnType(_type);
		if (this.elseBranch != null)
			this.elseBranch.setReturnType(_type);
	}

	@Override
	public void setParamsLength(int _paramOffset) {
		this.thenBranch.setParamsLength(_paramOffset);
		if (this.elseBranch != null)
			this.elseBranch.setParamsLength(_paramOffset);
	}

	@Override
	public void setInstance(Classe declaration) {
		this.condition.setInstance(declaration);
		this.thenBranch.setInstance(declaration);
		if (this.elseBranch != null) {
			this.elseBranch.setInstance(declaration);
		}
	}

}
