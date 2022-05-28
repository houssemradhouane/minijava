package fr.n7.stl.minijava.ast.element;

import fr.n7.stl.minijava.ast.instruction.Instruction;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public interface Element {
	
	public boolean collect(HierarchicalScope<Declaration> _scope);

	public boolean resolve(HierarchicalScope<Declaration> _scope);
	
	public boolean checkType();
	
	public int allocateMemory(Register _register, int _offset);
	
	public Fragment getCode(TAMFactory _factory);

}
