package com.greenpepper.call;

import com.greenpepper.Annotatable;
import com.greenpepper.annotation.WrongAnnotation;

public class AnnotateWrong implements Stub
{
    private Annotatable annotatable;
    private final boolean detailed;

    public AnnotateWrong( Annotatable annotatable, boolean withDetails )
    {
        this.annotatable = annotatable;
        this.detailed = withDetails;
    }

    public void call( Result result )
    {
        WrongAnnotation wrong = new WrongAnnotation();
        if (detailed) wrong.giveDetails( result.getExpected(), result.getActual() );
        annotatable.annotate( wrong );
    }
}
