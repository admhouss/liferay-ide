/*******************************************************************************
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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
 * Contributors:
 *    Kamesh Sampath - initial implementation
 *    Cindy Li - IDE-692
 ******************************************************************************/

package com.liferay.ide.eclipse.project.core;

import com.liferay.ide.eclipse.project.core.util.ProjectUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

/**
 * @author <a href="mailto:kamesh.sampath@hotmail.com">Kamesh Sampath</a>
 */
public class BinaryProjectImportDataModelProvider extends SDKProjectsImportDataModelProvider {

    
    /*
     * (non-Javadoc)
     * @see com.liferay.ide.eclipse.project.core.SDKProjectsImportDataModelProvider#createProjectErrorStatus()
     */
    @Override
    public IStatus createSelectedProjectsErrorStatus() {
        return ProjectCorePlugin.createErrorStatus( "Select a binary to import." );
    }

	/*
	 * (non-Javadoc)
	 * @see com.liferay.ide.eclipse.project.core.LiferayProjectImportDataModelProvider#getDefaultOperation()
	 */
	@Override
	public IDataModelOperation getDefaultOperation() {
		return new BinaryProjectImportOperation( this.model );
	}	

    @Override
    public void init() {
        super.init();

        ProjectUtil.setDefaultRuntime(getDataModel());
    }

}
