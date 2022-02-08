/*
 * Skytils - Hypixel Skyblock Quality of Life Mod
 * Copyright (C) 2022 Skytils
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mattyhd0.event;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skytils.skytilsmod.Skytils;
import skytils.skytilsmod.core.Config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ItemCreationDate {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event){

        if (event.itemStack == null || event.toolTip == null || event.entityPlayer == null) {
            return;
        }

        NBTTagCompound extraAttributes = event.itemStack.getSubCompound("ExtraAttributes", false);
        if(Skytils.config.getItemCreationDate() && extraAttributes != null && extraAttributes.hasKey("timestamp")){

            String timestamp = extraAttributes.getString("timestamp");
            LocalDateTime creationDate;

            try {
                if (timestamp.endsWith("M")) {
                    // format: month > day > year + 12 hour clock (AM or PM)
                    creationDate = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("M/d/yy h:mm a", Locale.US));
                } else {
                    // format: day > month > year + 24 hour clock (very, very rare)
                    creationDate = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("d/M/yy HH:mm", Locale.US));
                }
            } catch (Exception e){
                creationDate = null;
            }

            if(creationDate != null) {
                ZonedDateTime dateTime = ZonedDateTime.of(creationDate, ZoneId.systemDefault());
                event.toolTip.add("§6Creation date: §b"+dateTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }

        }

    }

}
