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
package com.liferay.ide.eclipse.service.core.model.internal;

import com.liferay.ide.eclipse.service.core.model.IColumn;

import org.eclipse.sapphire.DisposeEvent;
import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ImageData;
import org.eclipse.sapphire.modeling.ModelPropertyChangeEvent;
import org.eclipse.sapphire.modeling.ModelPropertyListener;
import org.eclipse.sapphire.services.ImageService;
import org.eclipse.sapphire.services.ImageServiceData;


public class ColumnImageService extends ImageService {

	private static final ImageData IMG_COLUMN = ImageData.readFromClassLoader(
		ColumnImageService.class, "images/column_16x16.gif");
	private static final ImageData IMG_COLUMN_PRIMARY = ImageData.readFromClassLoader(
		ColumnImageService.class, "images/column_primary_16x16.png");

	private ModelPropertyListener listener;

	@Override
	protected void initImageService() {

		this.listener = new ModelPropertyListener() {

			@Override
			public void handlePropertyChangedEvent(final ModelPropertyChangeEvent event) {
				refresh();
			}
		};

		context( IModelElement.class ).addListener( this.listener, IColumn.PROP_PRIMARY.getName() );

		attach( new Listener() {

			@Override
			public void handle( Event event ) {
				if ( event instanceof DisposeEvent ) {
					context( IModelElement.class ).removeListener( listener, IColumn.PROP_PRIMARY.getName() );
				}
			}

		} );
	}

	@Override
	public ImageServiceData compute() {
		ImageData imageData = null;

		if ( ( context( IColumn.class ) ).isPrimary().getContent() ) {
			imageData = IMG_COLUMN_PRIMARY;
		}
		else {
			imageData = IMG_COLUMN;
		}

		return new ImageServiceData( imageData );
	}

}
