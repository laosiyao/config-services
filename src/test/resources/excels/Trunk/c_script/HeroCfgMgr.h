#pragma once
#include "common/ThirdParties.h"

class TestTableCfg
{
public:
	TestTableCfg()
	{
	    Id= 0;
	    Quality= 0;
	    Race= 0;
	    Sex= 0;
	    Profession= 0;
	    NormalAttackId= 0;
	    Active1= 0;
	    Active2= 0;
	    Innate1= 0;
	    
	}

	~TestTableCfg(){};

public:
    
    int Id;
    int Quality;
    int Race;
    int Sex;
    int Profession;
    Array Pawncreate;
    json Skin;
    int NormalAttackId;
    int Active1;
    int Active2;
    int Innate1;
};

