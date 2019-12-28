package com.p4u.parvarish.Attandence.student;

import android.widget.TextView;

public class AttandenceViewHolder
{
    private TextView name;
    private TextView admission;
    private TextView status;
    public AttandenceViewHolder()
    {
    }

    public AttandenceViewHolder(TextView name, TextView admission, TextView status)
    {
        this.status = status;
        this.name = name;
        this.admission = admission;
    }


    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getAdmission() {
        return admission;
    }

    public void setAdmission(TextView admission) {
        this.admission = admission;
    }

    public TextView getStatus() {
        return status;
    }

    public void setStatus(TextView status) {
        this.status = status;
    }
}

