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
 *******************************************************************************/

package com.liferay.ide.eclipse.layouttpl.ui.policies;

import com.liferay.ide.eclipse.layouttpl.ui.cmd.PortletColumnChangeConstraintCommand;
import com.liferay.ide.eclipse.layouttpl.ui.cmd.PortletColumnCreateCommand;
import com.liferay.ide.eclipse.layouttpl.ui.cmd.PortletLayoutCreateCommand;
import com.liferay.ide.eclipse.layouttpl.ui.draw2d.FeedbackRoundedRectangle;
import com.liferay.ide.eclipse.layouttpl.ui.model.LayoutConstraint;
import com.liferay.ide.eclipse.layouttpl.ui.model.LayoutTplDiagram;
import com.liferay.ide.eclipse.layouttpl.ui.model.PortletColumn;
import com.liferay.ide.eclipse.layouttpl.ui.model.PortletLayout;
import com.liferay.ide.eclipse.layouttpl.ui.parts.PortletColumnEditPart;
import com.liferay.ide.eclipse.layouttpl.ui.parts.PortletLayoutEditPart;
import com.liferay.ide.eclipse.layouttpl.ui.util.LayoutTplUtil;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Greg Amerson
 */
@SuppressWarnings("unchecked")
public class PortletLayoutLayoutEditPolicy extends ConstrainedLayoutEditPolicy {

	// public static final int DEFAULT_FEEDBACK_HEIGHT = 100;

	protected IFigure feedbackFigure;

	public PortletLayoutLayoutEditPolicy() {
		super();
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		if (constraint instanceof LayoutConstraint && child instanceof PortletColumnEditPart) {
			PortletColumnEditPart portletColumnPart = (PortletColumnEditPart) child;
			PortletLayoutEditPart parentPart = (PortletLayoutEditPart) portletColumnPart.getParent();
			return new PortletColumnChangeConstraintCommand(
				portletColumnPart.getCastedModel(), (PortletLayout) parentPart.getModel(),
				(PortletLayout) parentPart.getModel(), (LayoutConstraint) constraint);
		}

		return null;
	}

	protected IFigure createLayoutFeedbackFigure(Request request) {
		Figure feedback = null;

		if (request instanceof CreateRequest) {
			boolean isRowRequest = LayoutTplUtil.isCreateRequest(PortletLayout.class, request);
			boolean isColumnRequest = LayoutTplUtil.isCreateRequest(PortletColumn.class, request);
			LayoutConstraint constraint = (LayoutConstraint) getConstraintFor((CreateRequest) request);
			feedback = new FeedbackRoundedRectangle();

			Rectangle partBounds = getPart().getFigure().getBounds().getCopy();

			if (isRowRequest) {
				feedback.setSize(getContainerWidth(), LayoutTplDiagramLayoutEditPolicy.DEFAULT_FEEDBACK_HEIGHT);

				PortletLayoutEditPart layoutEditPart = (PortletLayoutEditPart) getHost();
				int currentRowIndex = LayoutTplUtil.getRowIndex(layoutEditPart);
				if (constraint.newRowIndex == currentRowIndex) {
					partBounds.y -= (feedback.getSize().height / 2);
				}
				else if (constraint.newRowIndex > currentRowIndex || constraint.newRowIndex == -1) {
					partBounds.y = partBounds.y + partBounds.height - (feedback.getSize().height / 2);
				}

				feedback.setLocation(new Point(partBounds.x, partBounds.y));
			}
			else if (isColumnRequest) {
				feedback.setSize((int) (PortletLayoutEditPart.COLUMN_SPACING * 1.2), partBounds.height -
					(PortletLayoutEditPart.LAYOUT_MARGIN * 2));
				Point rectLocation = null;

				List children = getPart().getChildren();

				if (constraint.newColumnIndex >= children.size()) {
					PortletColumnEditPart insertColumnPart =
						(PortletColumnEditPart) getPart().getChildren().get(constraint.newColumnIndex - 1);
					Rectangle insertColumnRect = insertColumnPart.getFigure().getBounds();
					rectLocation = new Point(insertColumnRect.x + insertColumnRect.width, insertColumnRect.y);
				}
				else {
					int index = 0;
					if (constraint.newColumnIndex > -1) {
						index = constraint.newColumnIndex;
					}
					else {
						index = getPart().getChildren().size() - 1;
					}

					PortletColumnEditPart insertColumnPart = (PortletColumnEditPart) getPart().getChildren().get(index);
					Rectangle insertColumnRect = insertColumnPart.getFigure().getBounds();
					rectLocation = new Point(insertColumnRect.x - feedback.getSize().width, insertColumnRect.y);
				}

				feedback.setLocation(rectLocation);
			}
		}
		else if (request instanceof ChangeBoundsRequest) {

		}

		return feedback;
	}

	protected int getContainerWidth() {
		return getPart().getFigure().getSize().width;
	}

	@Override
	protected void eraseLayoutTargetFeedback(Request request) {
		super.eraseLayoutTargetFeedback(request);

		if (feedbackFigure != null) {
			removeFeedback(feedbackFigure);
			getFeedbackLayer().repaint();
			feedbackFigure = null;
		}
	}

	@Override
	protected void showLayoutTargetFeedback(Request request) {
		// ColumnLayoutEditPolicy
		super.showLayoutTargetFeedback(request);

		IFigure feedback = createLayoutFeedbackFigure(request);

		if (feedback != null && !feedback.equals(feedbackFigure)) {
			if (feedbackFigure != null) {
				removeFeedback(feedbackFigure);
			}

			feedbackFigure = feedback;
			addFeedback(feedbackFigure);
		}
	}

	protected PortletLayoutEditPart getPart() {
		return (PortletLayoutEditPart) getHost();
	}


	@Override
	protected Object getConstraintFor(Point orgPoint) {
		LayoutConstraint constraint = new LayoutConstraint();

		PortletLayoutEditPart layoutEditPart = (PortletLayoutEditPart) getHost();
		int currentRowIndex = LayoutTplUtil.getRowIndex(layoutEditPart);
		constraint.rowIndex = currentRowIndex;

		List columns = layoutEditPart.getChildren();
		int numColumns = columns.size();

		Rectangle columnBounds = ((PortletColumnEditPart) columns.get(0)).getFigure().getBounds().getCopy();
		Point copyPoint = orgPoint.getCopy();
		copyPoint.translate(layoutEditPart.getFigure().getBounds().getLocation());

		int topColumnY = columnBounds.y;
		int bottomColumnY = topColumnY + ((PortletColumnEditPart) columns.get(0)).getFigure().getBounds().height;

		if (copyPoint.y > bottomColumnY) {
			constraint.newRowIndex = currentRowIndex + 1;
		}
		else if (copyPoint.y < topColumnY) {
			constraint.newRowIndex = currentRowIndex;
		}
		else {
			// if (copyPoint.y > topColumnY && copyPoint.y < (topColumnY +
			// (columnBounds.height / 2))) {
			// constraint.newRowIndex = numColumns > 1 ? constraint.rowIndex - 1
			// : 0;
			// }
			// else {
			// constraint.newRowIndex = currentRowIndex + 1;
			// }

			// either need to insert this column at the first or the end
			if (orgPoint.x < 0) {
				constraint.newColumnIndex = 0;
			}
			else {
				for (int i = 0; i < columns.size(); i++) {
					int xCoord = ((PortletColumnEditPart) columns.get(i)).getFigure().getBounds().x;

					if (orgPoint.x < xCoord) {
						constraint.newColumnIndex = i;
						break;
					}
				}

				if (constraint.newColumnIndex == -1) {
					constraint.newColumnIndex = numColumns;
				}
			}

			// for the weight lets cut in half the column just inserted next two
			PortletColumnEditPart refColumnPart = null;
			if (constraint.newColumnIndex < numColumns) {
				refColumnPart = (PortletColumnEditPart) columns.get(constraint.newColumnIndex);
			}
			else {
				refColumnPart = (PortletColumnEditPart) columns.get(constraint.newColumnIndex - 1);
			}

			int refWeight = refColumnPart.getCastedModel().getWeight();
			int newWeight = -1;
			if (refWeight != PortletColumn.DEFAULT_WEIGHT) {
				newWeight = refWeight / 2;
			}
			else {
				newWeight = 50; // 50%
			}

			constraint.weight = LayoutTplUtil.adjustWeight(newWeight);
			constraint.refColumn = refColumnPart.getCastedModel();
		}

		return constraint;
	}

	@Override
	protected Object getConstraintFor(Rectangle rect) {
		PortletLayoutEditPart layoutEditPart = (PortletLayoutEditPart) getHost();
		List columns = layoutEditPart.getChildren();
		int numColumns = columns.size();

		LayoutConstraint constraint = new LayoutConstraint();
		constraint.rowIndex = LayoutTplUtil.getRowIndex(layoutEditPart);


		// either need to insert this column at the first or the end
		if (rect.x < 0) {
			constraint.newColumnIndex = 0;
		}
		else {
			for (int i = 0; i < columns.size(); i++) {
				int xCoord = ((PortletColumnEditPart) columns.get(i)).getFigure().getBounds().x;

				if (rect.x < xCoord) {
					constraint.newColumnIndex = i;
					break;
				}
			}

			if (constraint.newColumnIndex == -1) {
				constraint.newColumnIndex = numColumns;
			}
		}

		PortletColumnEditPart refColumnPart = null;
		if (constraint.newColumnIndex > 0) {
			refColumnPart = (PortletColumnEditPart) columns.get(constraint.newColumnIndex - 1);
		}
		else if (constraint.newColumnIndex == 0) {
			refColumnPart = (PortletColumnEditPart) columns.get(1);
		}

		constraint.refColumn = refColumnPart.getCastedModel();

		// get new weight based on resize
		int rowWidth = getHostFigure().getSize().width - (PortletLayoutEditPart.LAYOUT_MARGIN * 2);
		constraint.weight = LayoutTplUtil.adjustWeight((int) ((double) rect.width / (double) rowWidth * 100d));

		return constraint;
	}

	protected LayoutTplDiagram getDiagram() {
		return (LayoutTplDiagram) getHost().getParent().getModel();
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Object childClass = request.getNewObjectType();

		if (childClass == PortletColumn.class) {
			return new PortletColumnCreateCommand(
				(PortletColumn) request.getNewObject(), getDiagram(), (LayoutConstraint) getConstraintFor(request));
		}

		if (childClass == PortletLayout.class) {
			return new PortletLayoutCreateCommand(
				(PortletLayout) request.getNewObject(), (LayoutTplDiagram) getHost().getParent().getModel(),
				(LayoutConstraint) getConstraintFor(request));
		}

		return null;
	}

	@Override
	protected Command getMoveChildrenCommand(Request request) {
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		// return new RoundedRectangleEditPolicy();
		return super.createChildEditPolicy(child);
	}

}
