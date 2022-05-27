package fr.n7.stl.minijava.ast.element;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minijava.ast.cElement.ClassElement;
import fr.n7.stl.minijava.ast.scope.Declaration;
import fr.n7.stl.minijava.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.scope.Scope;
import fr.n7.stl.minijava.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class Classe implements Element, Scope<ClassElement>, Declaration {

	private String nom;
	private List<ClassElement> cElements;

	public Classe(String nom, List<ClassElement> cElements) {
		this.nom = nom;
		this.cElements = cElements;
	}

	@Override
	public String getName() {
		return this.nom;
	}

	@Override
	public Type getType() {
		return null;
	}
	
	@Override
	public ClassElement get(String _name) {
		boolean _found = false;
		Iterator<ClassElement> _iter = this.cElements.iterator();
		ClassElement _current = null;
		while (_iter.hasNext() && (! _found)) {
			_current = _iter.next();
			_found = _found || _current.getName().contentEquals(_name);
		}
		if (_found) {
			return _current;
		} else {
			return null;
		}
	}

	@Override
	public boolean contains(String _name) {
		boolean _result = false;
		Iterator<ClassElement> _iter = this.cElements.iterator();
		while (_iter.hasNext() && (! _result)) {
			_result = _result || _iter.next().getName().contentEquals(_name);
		}
		return _result;
	}

	@Override
	public boolean accepts(ClassElement _declaration) {
		return ! this.contains(_declaration.getName());
	}

	@Override
	public void register(ClassElement _declaration) {
		if (this.accepts(_declaration)) {
			this.cElements.add(_declaration);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean collect(HierarchicalScope<Declaration> _scope) {
		boolean ret = _scope.accepts(this);
		if (ret) {
			_scope.register(this);
			for (ClassElement e : this.cElements) {
				ret = ret && e.collect(_scope);
			}
		}
		return ret;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean ret = true;
		for (ClassElement e : this.cElements) {
			ret = ret && e.resolve(_scope);
		}
		return ret;
	}

	@Override
	public boolean checkType() {
		boolean ret = true;
		for (ClassElement e : this.cElements) {
			ret = ret && e.checkType();
		}
		return ret;
	}

	@Override
	public void allocateMemory(Register _register, int _offset) {
		for (ClassElement e : this.cElements) {
			e.allocateMemory(_register, _offset);
		}
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment ret = _factory.createFragment();
		for (ClassElement e : this.cElements) {
			ret.append(e.getCode(_factory));
		}
		return ret;
	}

}
