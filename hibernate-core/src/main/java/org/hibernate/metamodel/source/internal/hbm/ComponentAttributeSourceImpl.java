/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2012, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.metamodel.source.internal.hbm;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.EntityMode;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbAnyElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbArrayElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbBagElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbComponentElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbDynamicComponentElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbListElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbManyToOneElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbMapElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbOneToOneElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbPrimitiveArrayElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbPropertyElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbSetElement;
import org.hibernate.metamodel.source.internal.jaxb.hbm.JaxbTuplizerElement;
import org.hibernate.metamodel.source.spi.AttributeSource;
import org.hibernate.metamodel.source.spi.AttributeSourceContainer;
import org.hibernate.metamodel.source.spi.RelationalValueSource;
import org.hibernate.metamodel.spi.binding.SingularAttributeBinding;

/**
 * @author Steve Ebersole
 */
class ComponentAttributeSourceImpl extends AbstractComponentAttributeSourceImpl {
	public ComponentAttributeSourceImpl(
			MappingDocument sourceMappingDocument,
			JaxbComponentElement componentElement,
			AttributeSourceContainer parentContainer,
			String logicalTableName,
			SingularAttributeBinding.NaturalIdMutability naturalIdMutability) {
		super( sourceMappingDocument, componentElement, parentContainer, logicalTableName, naturalIdMutability );
	}

	protected JaxbComponentElement componentElement() {
		return (JaxbComponentElement) super.componentSourceElement();
	}

	@Override
	protected List<AttributeSource> buildAttributeSources() {
		List<AttributeSource> attributeSources = new ArrayList<AttributeSource>();
		for(final JaxbPropertyElement element : componentElement().getProperty()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbManyToOneElement element : componentElement().getManyToOne()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbOneToOneElement element: componentElement().getOneToOne()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbComponentElement element: componentElement().getComponent()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbDynamicComponentElement element: componentElement().getDynamicComponent()){
			attributeSources.add( buildAttributeSource(element) );
		}
		for(final JaxbAnyElement element: componentElement().getAny()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbMapElement element: componentElement().getMap()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbSetElement element: componentElement().getSet()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbListElement element: componentElement().getList()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbBagElement element: componentElement().getBag()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbArrayElement element: componentElement().getArray()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		for(final JaxbPrimitiveArrayElement element: componentElement().getPrimitiveArray()){
			attributeSources.add( buildAttributeSource( element ) );
		}
		return attributeSources;
	}

	@Override
	public String getParentReferenceAttributeName() {
		return componentElement().getParent() == null ? null : componentElement().getParent().getName();
	}

	@Override
	public String getExplicitTuplizerClassName() {
		if ( componentElement().getTuplizer() == null ) {
			return null;
		}
		final EntityMode entityMode = StringHelper.isEmpty( componentElement().getClazz() ) ? EntityMode.MAP : EntityMode.POJO;
		for ( JaxbTuplizerElement tuplizerElement : componentElement().getTuplizer() ) {
			if ( entityMode == EntityMode.parse( tuplizerElement.getEntityMode().value() ) ) {
				return tuplizerElement.getClazz();
			}
		}
		return null;
	}

	@Override
	public PropertyGeneration getGeneration() {
		// todo : is this correct here?
		return null;
	}

	@Override
	public boolean isLazy() {
		return componentElement().isLazy();
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return componentElement().isOptimisticLock();
	}

	@Override
	public boolean areValuesIncludedInInsertByDefault() {
		return componentElement().isInsert();
	}

	@Override
	public boolean areValuesIncludedInUpdateByDefault() {
		return componentElement().isUpdate();
	}

	@Override
	public boolean areValuesNullableByDefault() {
		return true;
	}

	@Override
	public List<RelationalValueSource> relationalValueSources() {
		// none, they are defined on the simple sub-attributes
		return null;
	}
}
