/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.scene;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.PGNode;
import com.sun.javafx.sg.PGParallelCamera;
import com.sun.javafx.tk.Toolkit;

/**
 * Specifies a parallel camera for rendering a scene without perspective correction. 
 * 
 * <p>If a scene contains only 2D transforms, then the following details are not
 * relevant. 
 * This camera defines a viewing volume for a parallel (orthographic) projection;
 * a rectangular box. This camera is always located at center of the window and
 * looks along the positive z-axis. The coordinate system defined by this camera
 * has its origin in the upper left corner of the panel with the Y-axis pointing
 * down and the Z axis pointing away from the viewer (into the screen). The
 * units are in pixel coordinates.
 *
 * @since JavaFX 1.3
 */
public class ParallelCamera extends Camera {

    @Override
    Camera copy() {
        ParallelCamera c = new ParallelCamera();
        c.setNearClip(getNearClip());
        c.setFarClip(getFarClip());
        return c;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    protected PGNode impl_createPGNode() {
        PGParallelCamera pgCamera = Toolkit.getToolkit().createPGParallelCamera();
        pgCamera.setNearClip((float) getNearClip());
        pgCamera.setFarClip((float) getFarClip());
        return pgCamera;
    }

    @Override
    final PickRay computePickRay(double x, double y, PickRay pickRay) {
        return PickRay.computeParallelPickRay(x, y, getCameraTransform(),
                //TODO: use actual clips after rendering uses them
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, pickRay);
    }

    @Override
    void computeProjectionTransform(GeneralTransform3D proj) {
        final double viewWidth = getViewWidth();
        final double viewHeight = getViewHeight();
        final double halfDepth =
                (viewWidth > viewHeight) ? viewWidth / 2.0 : viewHeight / 2.0;

        proj.ortho(0.0, viewWidth, viewHeight, 0.0, -halfDepth, halfDepth);
    }

    @Override
    protected void computeViewTransform(Affine3D view) {
        view.setToIdentity();
    }

    @Override
    Vec3d computePosition(Vec3d position) {
        if (position == null) {
            position = new Vec3d();
        }

        position.set(0.0, 0.0, -1.0);
        return position;
    }
}
