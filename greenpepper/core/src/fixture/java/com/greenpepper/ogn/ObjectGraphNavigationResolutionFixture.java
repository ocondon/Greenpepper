/*
 * Copyright (c) 2007 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.ogn;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.greenpepper.reflect.Message;

public class ObjectGraphNavigationResolutionFixture
{
    public String header;

    public ObjectGraphNavigationResolutionFixture(String header)
    {
        this.header = header;
    }

    public Collection<ObjectGraphNavigationInfo> query()
    {
		final List<ObjectGraphNavigationInfo> infos = new LinkedList<ObjectGraphNavigationInfo>();

		ObjectGraphNavigation objectGraphNavigation = new ObjectGraphNavigation(
				false,
				new ObjectGraphNavigationMessageResolver() {
					public Message resolve(ObjectGraphNavigationInfo info) {
						infos.add(info);
						return null;
					}
				});
		objectGraphNavigation.resolveMessage(header);
		return infos;
    }
}
