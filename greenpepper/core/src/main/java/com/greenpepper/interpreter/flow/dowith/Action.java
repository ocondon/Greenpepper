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

package com.greenpepper.interpreter.flow.dowith;

import static com.greenpepper.util.CollectionUtil.even;

import java.util.List;

import com.greenpepper.Example;
import com.greenpepper.interpreter.flow.AbstractAction;
import com.greenpepper.util.CollectionUtil;
import com.greenpepper.util.ExampleUtil;

public class Action extends AbstractAction
{
    public Action( Iterable<String> cells )
    {
        super(cells);
    }

    public static Action parse( Iterable<Example> cells )
    {
        return new Action( ExampleUtil.contentAsList(cells) );
    }

    protected List<String> keywords()
    {
        return even( getCells() );
    }

    protected List<String> parameters()
    {
        return CollectionUtil.odd( getCells() );
    }
}