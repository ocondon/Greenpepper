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

import com.greenpepper.TypeConversion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StaticInvocation extends Message
{
    private final Object target;
    private final Method method;

    public StaticInvocation( Object target, Method method )
    {
        super();

        this.target = target;
        this.method = method;
    }

    public int getArity()
    {
        return method.getParameterTypes().length;
    }

    public Object send( String... args ) throws Exception
    {
        try
        {
            return method.invoke( target, convert( args ) );
        }
        catch (InvocationTargetException e)
        {
            throw new SystemUnderDevelopmentException( e.getCause() );
        }
    }

    private Object[] convert( String... args )
    {
        assertArgumentsCount( args );
        return TypeConversion.convert( args, method.getParameterTypes() );
    }
}
