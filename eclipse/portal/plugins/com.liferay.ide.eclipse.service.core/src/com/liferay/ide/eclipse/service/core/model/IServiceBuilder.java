/*******************************************************************************
 * Copyright (c) 2010-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/
package com.liferay.ide.eclipse.service.core.model;

import com.liferay.ide.eclipse.service.core.model.internal.ShowRelationshipLabelsBinding;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.CustomXmlRootBinding;
import org.eclipse.sapphire.modeling.xml.annotations.CustomXmlValueBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlDocumentType;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

@GenerateImpl
@XmlDocumentType( publicId = "-//Liferay//DTD Service Builder 6.0.0//EN", systemId = "http://www.liferay.com/dtd/liferay-service-builder_6_0_0.dtd" )
@XmlBinding( path = "service-builder" )
public interface IServiceBuilder extends IModelElement {

	ModelElementType TYPE = new ModelElementType(IServiceBuilder.class);
	
	// *** Package-path ***

	@XmlBinding(path = "@package-path")
	@Label(standard = "&Package path")
	ValueProperty PROP_PACKAGE_PATH = new ValueProperty(TYPE, "PackagePath");

	Value<String> getPackagePath();

	void setPackagePath(String value);

	// *** Auto-Namespace-Tables ***

	@Type(base = Boolean.class)
	@Label(standard = "&Auto namespace tables")
	@XmlBinding(path = "@auto-namespace-tables")
	ValueProperty PROP_AUTO_NAMESPACE_TABLES = new ValueProperty(TYPE, "AutoNamespaceTables");

	Value<Boolean> isAutoNamespaceTables();

	void setAutoNamespaceTables(String value);

	void setAutoNamespaceTables(Boolean value);

	// *** Author ***
    
	@XmlBinding(path = "author")
	@Label(standard = "&Author")
	ValueProperty PROP_AUTHOR = new ValueProperty(TYPE, "Author");

	Value<String> getAuthor();

	void setAuthor(String value);

	// *** namespace ***

	@XmlBinding(path = "namespace")
	@Label(standard = "&Namespace")
	ValueProperty PROP_NAMESPACE = new ValueProperty(TYPE, "Namespace");

	Value<String> getNamespace();

	void setNamespace(String value);

	// *** Entities ***

	@Type(base = IEntity.class)
	@Label(standard = "Entities")
	@XmlListBinding(mappings = @XmlListBinding.Mapping(element = "entity", type = IEntity.class))
	ListProperty PROP_ENTITIES = new ListProperty(TYPE, "Entities");

	ModelElementList<IEntity> getEntities();

	// *** Exceptions ***

	@Type(base = IException.class)
	@Label(standard = "exceptions")
	@XmlListBinding(path = "exceptions", mappings = @XmlListBinding.Mapping(element = "exception", type = IException.class))
	ListProperty PROP_EXCEPTIONS = new ListProperty(TYPE, "Exceptions");

	ModelElementList<IException> getExceptions();

	@Type(base = IServiceBuilderImport.class)
	@Label(standard = "service builder imports")
	@XmlListBinding(mappings = @XmlListBinding.Mapping(element = "service-builder-import", type = IServiceBuilderImport.class))
	ListProperty PROP_SERVICE_BUILDER_IMPORTS = new ListProperty(TYPE, "ServiceBuilderImports");

	ModelElementList<IServiceBuilderImport> getServiceBuilderImports();

	@Type( base = Boolean.class )
	@DefaultValue( text = "true" )
	@CustomXmlValueBinding( impl = ShowRelationshipLabelsBinding.class )
	ValueProperty PROP_SHOW_RELATIONSHIP_LABELS = new ValueProperty( TYPE, "ShowRelationshipLabels" );

	Value<Boolean> getShowRelationshipLabels();

	void setShowRelationshipLabels( String value );

	void setShowRelationshipLabels( Boolean value );

}
