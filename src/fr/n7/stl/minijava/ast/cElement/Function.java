package fr.n7.stl.minijava.ast.cElement;

import java.util.List;

import fr.n7.stl.minijava.ast.instruction.declaration.ParameterDeclaration;

public interface Function extends ClassElement{
	
	public List<ParameterDeclaration> getParametres();
	
}
