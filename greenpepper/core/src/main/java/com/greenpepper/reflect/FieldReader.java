/*
 * Copyright (c) 2006 Pyxis Technologies inc.
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
package com.greenpepper.reflect;

import java.lang.reflect.Field;

/**
 * This <code>Action</code> implementation reads the value of a <code>class</code>
 * public <code>Field</code>.
 */
public class FieldReader extends Message
{
    private final Object target;
    private final Field field;

    public FieldReader( Object target, Field field )
    {
        super();
        
        this.target = target;
        this.field = field;
    }

    public Object send( String... args ) throws Exception
    {
        assertArgumentsCount( args );
        return field.get( target );
    }

    public int getArity()
    {
        return 0;
    }
}