/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020 shedaniel
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

package me.shedaniel.rei.api;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.rei.gui.ContainerScreenOverlay;
import me.shedaniel.rei.impl.ScreenHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ScreenWithHandler;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface AutoTransferHandler {
    
    /**
     * @return the priority of this handler, higher priorities will be called first.
     */
    default double getPriority() {
        return 0d;
    }
    
    Result handle(Context context);
    
    interface Result {
        static Result createSuccessful() {
            return new ResultImpl();
        }
        
        static Result createNotApplicable() {
            return new ResultImpl(false);
        }
        
        static Result createFailed(String errorKey) {
            return new ResultImpl(errorKey, new IntArrayList(), 1744764928);
        }
        
        static Result createFailedCustomButtonColor(String errorKey, int color) {
            return new ResultImpl(errorKey, new IntArrayList(), color);
        }
        
        static Result createFailed(String errorKey, IntList redSlots) {
            return new ResultImpl(errorKey, redSlots, 1744764928);
        }
        
        static Result createFailedCustomButtonColor(String errorKey, IntList redSlots, int color) {
            return new ResultImpl(errorKey, redSlots, color);
        }
        
        int getColor();
        
        boolean isSuccessful();
        
        boolean isApplicable();
        
        String getErrorKey();
        
        IntList getIntegers();
    }
    
    interface Context {
        static Context create(boolean actuallyCrafting, ScreenWithHandler<?> containerScreen, RecipeDisplay recipeDisplay) {
            return new ContextImpl(actuallyCrafting, containerScreen, () -> recipeDisplay);
        }
        
        default MinecraftClient getMinecraft() {
            return MinecraftClient.getInstance();
        }
        
        boolean isActuallyCrafting();
        
        ScreenWithHandler<?> getScreenWithHandler();
        
        @Deprecated
        @ApiStatus.ScheduledForRemoval
        default ScreenWithHandler<?> getContainerScreen() {
            return getScreenWithHandler();
        }
        
        RecipeDisplay getRecipe();
        
        @Deprecated
        @ApiStatus.ScheduledForRemoval
        default ScreenHandler getContainer() {
            return getScreenHandler();
        }
        
        default ScreenHandler getScreenHandler() {
            return getScreenWithHandler().getScreenHandler();
        }
        
        @Deprecated
        @ApiStatus.ScheduledForRemoval
        default ContainerScreenOverlay getOverlay() {
            return ScreenHelper.getLastOverlay();
        }
    }
    
    @ApiStatus.Internal
    final class ResultImpl implements Result {
        private boolean successful, applicable;
        private String errorKey;
        private IntList integers = new IntArrayList();
        private int color;
        
        private ResultImpl() {
            this.successful = true;
            this.applicable = true;
        }
        
        public ResultImpl(boolean applicable) {
            this.successful = false;
            this.applicable = applicable;
        }
        
        public ResultImpl(String errorKey, IntList integers, int color) {
            this.successful = false;
            this.applicable = true;
            this.errorKey = errorKey;
            if (integers != null)
                this.integers = integers;
            this.color = color;
        }
        
        @Override
        public int getColor() {
            return color;
        }
        
        @Override
        public boolean isSuccessful() {
            return successful;
        }
        
        @Override
        public boolean isApplicable() {
            return applicable;
        }
        
        @Override
        public String getErrorKey() {
            return errorKey;
        }
        
        @Override
        public IntList getIntegers() {
            return integers;
        }
    }
    
    @ApiStatus.Internal
    final class ContextImpl implements Context {
        boolean actuallyCrafting;
        ScreenWithHandler<?> screenWithHandler;
        Supplier<RecipeDisplay> recipeDisplaySupplier;
        
        private ContextImpl(boolean actuallyCrafting, ScreenWithHandler<?> screenWithHandler, Supplier<RecipeDisplay> recipeDisplaySupplier) {
            this.actuallyCrafting = actuallyCrafting;
            this.screenWithHandler = screenWithHandler;
            this.recipeDisplaySupplier = recipeDisplaySupplier;
        }
        
        @Override
        public boolean isActuallyCrafting() {
            return actuallyCrafting;
        }
        
        @Override
        public ScreenWithHandler<?> getScreenWithHandler() {
            return screenWithHandler;
        }
        
        @Override
        public RecipeDisplay getRecipe() {
            return recipeDisplaySupplier.get();
        }
    }
    
}
