/**
 * 
 */
package fr.n7.stl.minijava.ast.expression.assignable;

import fr.n7.stl.minijava.ast.SemanticsUndefinedException;
import fr.n7.stl.minijava.ast.cElement.FieldDeclaration;
import fr.n7.stl.minijava.ast.cElement.StaticFieldDeclaration;
import fr.n7.stl.minijava.ast.element.Classe;
import fr.n7.stl.minijava.ast.expression.AbstractField;
import fr.n7.stl.minijava.ast.expression.BinaryOperator;
import fr.n7.stl.minijava.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.tam.ast.TAMInstruction;
import fr.n7.stl.tam.ast.impl.TAMFactoryImpl;

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAssignment extends AbstractField implements AssignableExpression {

	/**
	 * Construction for the implementation of a record field assignment expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field assignment expression.
	 * @param _name Name of the field in the record field assignment expression.
	 */
	public FieldAssignment(AssignableExpression _record, String _name) {
		super(_record, _name);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.FieldAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		/*Fragment ret = _factory.createFragment();
		ret.append(this.expr.getCode(_factory));
		ret.add(_factory.createLoadL(this.field.getOffset()));
		ret.add(Library.IAdd);
		return ret;*/
		
		Fragment ret = _factory.createFragment();
		if (this.field instanceof StaticFieldDeclaration) {
			StaticFieldDeclaration decl = (StaticFieldDeclaration) this.field;
			ret.add(_factory.createLoadA(
					decl.getRegister(), 
					decl.getOffset()));
		} else if (this.field instanceof FieldDeclaration) {
			FieldDeclaration decl = (FieldDeclaration) this.field;
			ret.append(this.expr.getCode(_factory));
			if (this.expr instanceof AccessibleExpression) {
				ret.add(_factory.createLoadI(this.expr.getType().length()));
			}
			ret.add(_factory.createLoadL(decl.getOffset()));
			ret.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
		} else {
			throw new SemanticsUndefinedException("getCode not defined in FieldAssignment for this kind of field");
		}
		return ret;
	}

	@Override
	public void setInstance(Classe declaration) {
		this.expr.setInstance(declaration);
	}
	
}
