package com.sss.magicwheel.wheel.coversflow;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.ViewGroup;

import com.sss.magicwheel.wheel.misc.WheelComputationHelper;
import com.sss.magicwheel.wheel.misc.CoordinatesHolder;

/**
 * Store measurements for horizontal stripe view (covers flow).
 *
 * @author Alexey Kovalev
 * @since 24.02.2016.
 */
public final class CoversFlowListMeasurements {

    public static final double COVER_ASPECT_RATIO = 1.5;
    private static final int DEFAULT_HEIGHT_MAX_HEIGHT_DELTA = 50;

    private static final int INNER_TOP_PADDING = 15;
    private static final int INNER_BOTTOM_PADDING = 15;

    private static CoversFlowListMeasurements instance;

    private final Context context;

    private final int maxCoverHeight;
    private final Rect coverDefaultMargins;
    private final ViewGroup.MarginLayoutParams initialCoverLayoutParams;
    private final int leftOffset;
    private final float resizingEdgePosition;

    public static void initialize(WheelComputationHelper computationHelper, Context context) {
        instance = new CoversFlowListMeasurements(context, computationHelper);
    }

    public static CoversFlowListMeasurements getInstance() {
        return instance;
    }

    private final WheelComputationHelper computationHelper;

    private CoversFlowListMeasurements(Context context, WheelComputationHelper computationHelper) {
        this.context = context;
        this.computationHelper = computationHelper;
        instance = this;

        this.maxCoverHeight = (int) (
                getGapBottomRayPosition().y - getGapTopRayPosition().y - INNER_TOP_PADDING - INNER_BOTTOM_PADDING
        );
        final int coverDefaultHeight = maxCoverHeight - 2 * DEFAULT_HEIGHT_MAX_HEIGHT_DELTA;
        final int coverDefaultWidth = (int) (COVER_ASPECT_RATIO * coverDefaultHeight);

        this.initialCoverLayoutParams = new ViewGroup.MarginLayoutParams(coverDefaultWidth, coverDefaultHeight);

        this.coverDefaultMargins = new Rect(
                initialCoverLayoutParams.leftMargin,
                initialCoverLayoutParams.topMargin,
                initialCoverLayoutParams.rightMargin,
                initialCoverLayoutParams.bottomMargin
        );

        this.leftOffset = (int) computeCoverResizingEdgePosition();
        this.resizingEdgePosition = computeCoverResizingEdgePosition();
    }

    private float computeCoverResizingEdgePosition() {
        return getGapTopRayPosition().x + getCoverDefaultWidth() / 2;
    }

    public ViewGroup.MarginLayoutParams safeCopyInitialLayoutParams() {
        return new ViewGroup.MarginLayoutParams(initialCoverLayoutParams);
    }

    public int getCoverMaxHeight() {
        return maxCoverHeight;
    }

    public int getCoverDefaultHeight() {
        return initialCoverLayoutParams.height;
    }

    public int getCoverDefaultWidth() {
        return initialCoverLayoutParams.width;
    }

    public Rect getCoverDefaultMargins() {
        return coverDefaultMargins;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public int getRightOffset() {
        return computationHelper.getComputedScreenDimensions().getWidth() - leftOffset;
    }

    public float getResizingEdgePosition() {
        return resizingEdgePosition;
    }

    private PointF getGapTopRayPosition() {
        final PointF pos = CoordinatesHolder.ofPolar(computationHelper.getWheelConfig().getInnerRadius(),
                computationHelper.getWheelConfig().getAngularRestrictions().getGapAreaTopEdgeAngleRestrictionInRad()
        ).toPointF();

        return WheelComputationHelper.fromCircleCoordsSystemToRecyclerViewCoordsSystem(pos);
    }

    private PointF getGapBottomRayPosition() {
        final PointF pos = CoordinatesHolder.ofPolar(computationHelper.getWheelConfig().getInnerRadius(),
                computationHelper.getWheelConfig().getAngularRestrictions().getGapAreaBottomEdgeAngleRestrictionInRad()
        ).toPointF();

        return WheelComputationHelper.fromCircleCoordsSystemToRecyclerViewCoordsSystem(pos);
    }
}
