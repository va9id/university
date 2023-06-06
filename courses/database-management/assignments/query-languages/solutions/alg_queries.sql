-- 1
T1 := project C# (select NAME = 'Last' (Customer))
T2 := project B# (T1 njoin Account); 
T3 := project Name (T2 njoin Bank);
T3; 

-- 2
T4 := project B# (select NAME = 'Royal' (Bank)); 
T5 := project C# (T4 njoin Account); 
T6 := project Name (T5 njoin Customer);
T6;

-- 3
T7 := project C# (select BALANCE< 3000 (Account));
T8 := project Name (T7 njoin Customer); 
T8; 

-- 4
T9(B#, BName) := project B#, Name (Bank njoin Account);
T10(C#, CName) := project C#, Name (Customer njoin Account); 
T11 := project CName, BName (T9 njoin T10 njoin Account); 
T11; 

-- 5
T12 := project C# (Customer);
T13 := project C# (Account);
T14 := T12 minus T13; 
T15 := project Name (T14 njoin Customer); 
T15; 

-- 6
T16 := project B# (Bank); 
T17 := project B#, C# (Account); 
T18 := T17 divideby T16; 
T19 := project Name (Customer njoin T18); 
T19; 

-- 7
T20 := project B# (select Name != 'France' (Bank));
T21 := T17 divideby T20; 
T22 := T21 minus T18; 
T23 := project Name (T22 njoin Customer);

-- 8
T24 := project B# (select Name = 'Clark' (Customer njoin Account)); 
T25 := T17 divideby T24; 
T26 := project Name (Customer njoin T17); 
T26;

-- 9
T27 := T16 minus T24;
T28 := project C# (T27 njoin Account); 
T29 := T25 minus T28; 
T30 := project Name (Customer njoin T29);
T30; 

-- 10
T31(B#, c) := aggregate B#, count(*) (Account);
T32 := project C# ((select c > 2 (T31)) njoin Account);
T33 := project Name (T32 njoin Customer);
T33; 