/*
 * Copyright (c) 2016-2017 Me4502 (Madeline Miller)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.me4502.slotmachines.util;

import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.Optional;

public class LocationUtil {

    /**
     * @param face Start from direction
     * @return clockwise direction
     */
    public static Direction getClockWise(Direction face) {
        switch (face) {
            case NORTH:
                return Direction.EAST;
            case EAST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.WEST;
            case WEST:
                return Direction.NORTH;
            default:
                return face;
        }
    }

    /**
     * @param face Start from direction
     * @return clockwise direction
     */
    public static Direction getCounterClockWise(Direction face) {
        switch (face) {
            case NORTH:
                return Direction.WEST;
            case EAST:
                return Direction.NORTH;
            case SOUTH:
                return Direction.EAST;
            case WEST:
                return Direction.SOUTH;
            default:
                return face;
        }
    }

    public static Direction getFront(Location<?> sign) {
        Optional<Direction> data = sign.get(Keys.DIRECTION);

        return data.orElse(Direction.NONE);
    }

    public static Direction getBack(Location sign) {
        Direction front = getFront(sign);
        if (front == null) return Direction.NONE;

        return front.getOpposite();
    }

    public static Location<?> getBackBlock(Location<?> sign) {
        return sign.getRelative(getBack(sign));
    }

    /**
     * @param sign treated as sign post if it is such, or else assumed to be a wall sign (i.e.,
     * if you ask about a stone block, it's considered a wall
     * sign).
     * @return the cardinal or ordinal direction to a player's left as they face the sign to read it; if the sign is
     * oriented in a further direction,
     * the result is rounded to the nearest ordinal direction.
     */
    public static Direction getRight(Location<?> sign) {
        Direction front = getFront(sign);
        if (front == null) return Direction.NONE;

        return getClockWise(front);
    }

    public static Location<?> getLeftBlock(Location<?> sign) {
        return sign.getRelative(getLeft(sign));
    }

    /**
     * @param sign treated as sign post if it is such, or else assumed to be a wall sign (i.e.,
     * if you ask about a stone block, it's considered a wall
     * sign).
     * @return the cardinal or ordinal direction to a player's right they face the sign to read it; if the sign is
     * oriented in a further direction, the
     * result is rounded to the nearest ordinal direction.
     */
    public static Direction getLeft(Location<?> sign) {
        Direction front = getFront(sign);
        if (front == null) return Direction.NONE;

        return getCounterClockWise(front);
    }

    public static Location<?> getRightBlock(Location<?> sign) {
        return sign.getRelative(getRight(sign));
    }

    public static String getTextRaw(Sign sign, int line) {
        return getTextRaw(sign.get(SignData.class).get(), line);
    }

    public static String getTextRaw(SignData sign, int line) {
        Text text = getText(sign, line);
        return text.toPlain();
    }

    public static Text getText(SignData sign, int line) {
        return sign.lines().get(line);
    }
}
