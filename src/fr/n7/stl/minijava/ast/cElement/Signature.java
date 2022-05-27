package fr.n7.stl.minijava.ast.cElement;

import java.util.List;

import fr.n7.stl.minijava.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minijava.ast.type.Type;

public class Signature {
	
	private Type type;
	private String nom;
	private List<ParameterDeclaration> parametres;

	public Signature(Type type, String nom, List<ParameterDeclaration> parametres) {
		this.type = type;
		this.nom = nom;
		this.parametres = parametres;
	}

	public Type getType() {
		return type;
	}

	public String getNom() {
		return nom;
	}

	public List<ParameterDeclaration> getParametres() {
		return parametres;
	}
}
