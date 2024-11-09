/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022, 2023 shedaniel
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

package me.shedaniel.rei.plugin.common.networking;

import me.shedaniel.rei.plugin.common.displays.tag.TagNodes;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public record RequestTagsResponsePacket(UUID uuid, Map<ResourceLocation, TagNodes.TagData> values) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("roughlyenoughitems", "request_tags_response");
    public static final Type<RequestTagsResponsePacket> TYPE = new Type<>(ID);
    public static final StreamCodec<? super RegistryFriendlyByteBuf, RequestTagsResponsePacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            RequestTagsResponsePacket::uuid,
            ByteBufCodecs.map(
                    HashMap::new,
                    ResourceLocation.STREAM_CODEC,
                    TagNodes.TagData.CODEC
            ),
            RequestTagsResponsePacket::values,
            RequestTagsResponsePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return RequestTagsResponsePacket.TYPE;
    }
}
