#include "move.h"

#include <unistd.h>
#include <stdlib.h>
#include <assert.h>
#include <ncurses.h>
#include "dungeon.h"
#include "heap.h"
#include "move.h"
#include "npc.h"
#include "pc.h"
#include "character.h"
#include "utils.h"
#include "path.h"
#include "event.h"
#include "io.h"
#include "npc.h"
void heal(dungeon *d){
  if( d->PC->hp<1000&& d->PC->cdH==0&&d->PC->Mana>=10){
     d->PC->cdH=10;
    
    d->PC->hp+=10;
  d->PC->Mana-=10;
  io_queue_message("You healed %d damage",10);
  }
}
void do_combatS(dungeon *d, character *atk, character *def,pair_t dest){
   int damR = d->PC->damage->roll();
   
     for (int h =1;h<12;h++){
      if(h==2){
	h++;
      }
      if(d->PC->equip[h]){damR+=d->PC->equip[h]->roll_dice();
      }
     
    }
   
       dest[dim_x]-=1;
     
      dest[dim_y]-=1;
    for(int i=0;i<3;i++){
      for(int j=0;j<3;j++){
	
     
       

	
	if(charxy(j+dest[dim_x],i+dest[dim_y])&&charxy(j+dest[dim_x],i+dest[dim_y])->get_symbol()!='@'){
     charxy(j+dest[dim_x],i+dest[dim_y])->hp-=damR;
      io_queue_message("You delt %d damage",damR);
    
      if((int)charxy(j+dest[dim_x],i+dest[dim_y])->hp<=0){
	if(((npc *)charxy(j+dest[dim_x],i+dest[dim_y]))-> characteristics>=NPC_BOSS){
	 d->PC->Boss=0;
       }
charxy(j+dest[dim_x],i+dest[dim_y])->alive =0;
	d->num_monsters--;
	io_queue_message("You smite %s%s!", is_unique(charxy(j+dest[dim_x],i+dest[dim_y])) ? "" : "the ", charxy(j+dest[dim_x],i+dest[dim_y])->name);
charxy(j+dest[dim_x],i+dest[dim_y]) = NULL;

      }
	}
	

	
      }
       
    }
   
    d->PC->cdS=10;
    d->PC->Mana-=10;

}
void do_combatR(dungeon *d, character *atk, character *def)
{
    int hitR = rand()%101;
    int hitP = d->PC->hit;
     for (int h =1;h<12;h++){
	
      if(d->PC->equip[h]){hitP+=d->PC->equip[h]->get_hit();
      }
     
    }
     if(hitP>=hitR){
    
   int damR = d->PC->damage->roll();
    for (int h =1;h<12;h++){
	
      if(d->PC->equip[h]){damR+=d->PC->equip[h]->roll_dice();
      }
     
    }
     def->hp-=damR;
      io_queue_message("You delt %d damage",damR);
    
     if((int)def->hp<=0){
       if(((npc *)def)-> characteristics>=NPC_BOSS){
	 d->PC->Boss=0;
       }
	def->alive =0;
	d->num_monsters--;
	charpair(def->position) = NULL;
	 io_queue_message("You smite %s%s!", is_unique(def) ? "" : "the ", def->name);
      }
     }
     else{
io_queue_message("Your attack missed,you rolled %d against %d",hitP,hitR);
     }
}
void do_combat(dungeon *d, character *atk, character *def)
{
  // int can_see_atk, can_see_def;
  const char *organs[] = {
    "liver",                   /*  0 */
    "pancreas",                /*  1 */
    "heart",                   /*  2 */
    "eye",                     /*  3 */
    "arm",                     /*  4 */
    "leg",                     /*  5 */
    "intestines",              /*  6 */
    "gall bladder",            /*  7 */
    "lungs",                   /*  8 */
    "hand",                    /*  9 */
    "foot",                    /* 10 */
    "spinal cord",             /* 11 */
    "pituitary gland",         /* 12 */
    "thyroid",                 /* 13 */
    "tongue",                  /* 14 */
    "bladder",                 /* 15 */
    "diaphram",                /* 16 */
    "stomach",                 /* 17 */
    "pharynx",                 /* 18 */
    "esophagus",               /* 19 */
    "trachea",                 /* 20 */
    "urethra",                 /* 21 */
    "spleen",                  /* 22 */
    "ganglia",                 /* 23 */
    "ear",                     /* 24 */
    "subcutaneous tissue"      /* 25 */
    "cerebellum",              /* 26 */ /* Brain parts begin here */
    "hippocampus",             /* 27 */
    "frontal lobe",            /* 28 */
    "brain",                   /* 29 */
  };
  int part;

  if (def->alive) {
   
    // def->hp-=10;
     
    //  charpair(def->position) = NULL;
    
    if (def != d->PC) {
      // d->num_monsters--;
       }
    else {
      int defence = d->PC->defence;
      int dodge =  d->PC->dodge;
      int dogR = rand()%101;
       for (int h =0;h<12;h++){
      
	
      if(d->PC->equip[h]){defence+=d->PC->equip[h]->get_def();
	dodge+= d->PC->equip[h]->get_dog();
      }
     
    }
       int damR=atk->damage->roll()-(defence/10);
      if(damR<=0||dodge>dogR){
	io_queue_message("You took no damage");
      }else{
      d->PC->hp -=damR;
       
      io_queue_message("You took %d damage",damR);
      
      if((int)d->PC->hp<=0){
	d->PC->alive=0;
	charpair(d->PC->position) = NULL;
      if ((part = rand() % (sizeof (organs) / sizeof (organs[0]))) < 26) {
        io_queue_message("As %s%s eats your %s,", is_unique(atk) ? "" : "the ",
                         atk->name, organs[rand() % (sizeof (organs) /
                                                     sizeof (organs[0]))]);
        io_queue_message("   ...you wonder if there is an afterlife.");
        /* Queue an empty message, otherwise the game will not pause for *
         * player to see above.                                          */
        io_queue_message("");
      } else {
        io_queue_message("Your last thoughts fade away as "
                         "%s%s eats your %s...",
                         is_unique(atk) ? "" : "the ",
                         atk->name, organs[part]);
        io_queue_message("");
      }
      /* Queue an empty message, otherwise the game will not pause for *
       * player to see above.                                          */
      io_queue_message("");
    }
      }
    atk->kills[kill_direct]++;
    atk->kills[kill_avenged] += (def->kills[kill_direct] +
                                  def->kills[kill_avenged]);
    }
  }

  if (atk == d->PC) {
    int hitR = rand()%101;
    int hitP = d->PC->hit;
     for (int h =0;h<12;h++){
      if(h==2){
	h++;
      }
	
      if(d->PC->equip[h]){hitP+=d->PC->equip[h]->get_hit();
      }
     
    }
     if(hitP>=hitR){
    
    int damR = d->PC->damage->roll();
    for (int h =0;h<12;h++){
      if(h==2){
	h++;
      }
	
      if(d->PC->equip[h]){damR+=d->PC->equip[h]->roll_dice();
      }
     
    }
     def->hp-=damR;
      io_queue_message("You delt %d damage with a hit of %d against %d",damR,hitP,hitR);
    
     if((int)def->hp<=0){
       if(((npc *)def)-> characteristics>=NPC_BOSS){
	 d->PC->Boss=0;
       }
	def->alive =0;
	d->num_monsters--;
	charpair(def->position) = NULL;
	 io_queue_message("You smite %s%s!", is_unique(def) ? "" : "the ", def->name);
      }
     }else{
       io_queue_message("Your attack missed,you rolled %d against %d",hitP,hitR);
     }
   
  }
     if (atk != d->PC && def != d->PC) {
     //  io_queue_message("I have reached the forbidan lands.");
      int test=1,count=0;
       int prevx,prevy;
       prevx=def->position[dim_x];
       prevy= def->position[dim_y];
      while(test==1&&count!=1000){
	int ran = rand()%8;
	count++;
       if(ran==0&&charxy(prevx-1,prevy)==NULL&&mapxy(prevx-1,prevy)!=ter_wall){
      
       
	 def->position[dim_x]-=1;
	 // def->position[dim_y]-=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
	test=0;
      }
        else if(ran==1&&charxy(prevx-1,prevy+1)==NULL&&mapxy(prevx-1,prevy+1)!=ter_wall){
      
       
	  def->position[dim_x]-=1;
         def->position[dim_y]+=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
		test=0;
      }
       
        else if(ran==2&&charxy(prevx,prevy+1)==NULL&&mapxy(prevx,prevy+1)!=ter_wall){
      
       
	  //  def->position[dim_x]-=1
         def->position[dim_y]+=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
		test=0;
      }
        else if(ran==3&&charxy(prevx+1,prevy+1)==NULL&&mapxy(prevx+1,prevy+1)!=ter_wall){
      
       
	  def->position[dim_x]+=1;
         def->position[dim_y]+=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
		test=0;
      }
        else if(ran==4&&charxy(prevx+1,prevy)==NULL&&mapxy(prevx+1,prevy)!=ter_wall){
      
       
	  def->position[dim_x]+=1;
	 // def->position[dim_y]-=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
		test=0;
      }
        else if(ran==5&&charxy(prevx+1,prevy-1)==NULL&&mapxy(prevx+1,prevy-1)!=ter_wall){
      
       
	  def->position[dim_x]+=1;
         def->position[dim_y]-=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
		test=0;
      }
        else if(ran==6&&charxy(prevx,prevy-1)==NULL&&mapxy(prevx,prevy-1)!=ter_wall){
      
       
	  // def->position[dim_x]=1
         def->position[dim_y]-=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
		test=0;
      }
        else if(ran==7&&charxy(prevx-1,prevy-1)==NULL&&mapxy(prevx-1,prevy-1)!=ter_wall){
      
       
	  def->position[dim_x]-=1;
         def->position[dim_y]-=1;
	 charpair(def->position)=def;
        charxy(prevx,prevy) = NULL;
		test=0;
      }
       if(count==1000){
	 io_queue_message("I have reached the forbidan lands.");
	 charpair(def->position)=atk;
	 charpair(atk->position)=def;
	 test = 0;
	 break;
       }
      
   }
  
  


   }
}

void move_character(dungeon *d, character *c, pair_t next)
{
  if (charpair(next) &&
      ((next[dim_y] != c->position[dim_y]) ||
       (next[dim_x] != c->position[dim_x]))) {
    do_combat(d, c, charpair(next));
  } else {
    /* No character in new position. */

    d->character_map[c->position[dim_y]][c->position[dim_x]] = NULL;
    c->position[dim_y] = next[dim_y];
    c->position[dim_x] = next[dim_x];
    d->character_map[c->position[dim_y]][c->position[dim_x]] = c;
  }

  if (c == d->PC) {
    pc_reset_visibility(d->PC);
    pc_observe_terrain(d->PC, d);
  }
}

void do_moves(dungeon *d)
{
  pair_t next;
  character *c;
  event *e;

  /* Remove the PC when it is PC turn.  Replace on next call.  This allows *
   * use to completely uninit the heap when generating a new level without *
   * worrying about deleting the PC.                                       */

  if (pc_is_alive(d)) {
    /* The PC always goes first one a tie, so we don't use new_event().  *
     * We generate one manually so that we can set the PC sequence       *
     * number to zero.                                                   */
    e = (event *) malloc(sizeof (*e));
    e->type = event_character_turn;
    /* Hack: New dungeons are marked.  Unmark and ensure PC goes at d->time, *
     * otherwise, monsters get a turn before the PC.                         */
    if (d->is_new) {
      d->is_new = 0;
      e->time = d->time;
    } else {
      //  io_queue_message("You delt %d speed",d->PC->speed);
      e->time = d->time + (1000 / d->PC->speed);
    }
    e->sequence = 0;
    e->c = d->PC;
    heap_insert(&d->events, e);
  }

  while (pc_is_alive(d) &&
         (e = (event *) heap_remove_min(&d->events)) &&
         ((e->type != event_character_turn) || (e->c != d->PC))) {
    d->time = e->time;
    if (e->type == event_character_turn) {
      c = e->c;
    }
    if (!c->alive) {
      if (d->character_map[c->position[dim_y]][c->position[dim_x]] == c) {
        d->character_map[c->position[dim_y]][c->position[dim_x]] = NULL;
      }
      if (c != d->PC) {
        event_delete(e);
      }
      continue;
    }

    npc_next_pos(d, (npc *) c, next);
     move_character(d, (npc *) c, next);

    heap_insert(&d->events, update_event(d, e, 1000 / c->speed));
  }

  io_display(d);
  if (pc_is_alive(d) && e->c == d->PC) {
    c = e->c;
    d->time = e->time;
    /* Kind of kludgey, but because the PC is never in the queue when   *
     * we are outside of this function, the PC event has to get deleted *
     * and recreated every time we leave and re-enter this function.    */
    e->c = NULL;
    event_delete(e);
    io_handle_input(d);
  }
  if(d->PC->cdH!=0){
    d->PC->cdH-=1;
  }
  if(d->PC->cdS!=0){
    d->PC->cdS-=1;
      }
}

void dir_nearest_wall(dungeon *d, character *c, pair_t dir)
{
  dir[dim_x] = dir[dim_y] = 0;

  if (c->position[dim_x] != 1 && c->position[dim_x] != DUNGEON_X - 2) {
    dir[dim_x] = (c->position[dim_x] > DUNGEON_X - c->position[dim_x] ? 1 : -1);
  }
  if (c->position[dim_y] != 1 && c->position[dim_y] != DUNGEON_Y - 2) {
    dir[dim_y] = (c->position[dim_y] > DUNGEON_Y - c->position[dim_y] ? 1 : -1);
  }
}

uint32_t against_wall(dungeon *d, character *c)
{
  return ((mapxy(c->position[dim_x] - 1,
                 c->position[dim_y]    ) == ter_wall_immutable) ||
          (mapxy(c->position[dim_x] + 1,
                 c->position[dim_y]    ) == ter_wall_immutable) ||
          (mapxy(c->position[dim_x]    ,
                 c->position[dim_y] - 1) == ter_wall_immutable) ||
          (mapxy(c->position[dim_x]    ,
                 c->position[dim_y] + 1) == ter_wall_immutable));
}

uint32_t in_corner(dungeon *d, character *c)
{
  uint32_t num_immutable;

  num_immutable = 0;

  num_immutable += (mapxy(c->position[dim_x] - 1,
                          c->position[dim_y]    ) == ter_wall_immutable);
  num_immutable += (mapxy(c->position[dim_x] + 1,
                          c->position[dim_y]    ) == ter_wall_immutable);
  num_immutable += (mapxy(c->position[dim_x]    ,
                          c->position[dim_y] - 1) == ter_wall_immutable);
  num_immutable += (mapxy(c->position[dim_x]    ,
                          c->position[dim_y] + 1) == ter_wall_immutable);

  return num_immutable > 1;
}

static void new_dungeon_level(dungeon *d, uint32_t dir)
{
  /* Eventually up and down will be independantly meaningful. *
   * For now, simply generate a new dungeon.                  */

  switch (dir) {
  case '<':
  case '>':
    new_dungeon(d);
    break;
  default:
    break;
  }
}


uint32_t move_pc(dungeon *d, uint32_t dir)
{
  pair_t next;
  uint32_t was_stairs = 0;
  // int weapon,off,range,armor,hel,cloak,gloves,boot,amulet,light,ring;
  // int invC=0;
  // int equipC;
  const char *wallmsg[] = {
    "There's a wall in the way.",
    "BUMP!",
    "Ouch!",
    "You stub your toe.",
    "You can't go that way.",
    "You admire the engravings.",
    "Are you drunk?"
  };
  if(d->PC->Mana+2>100){
  }else{
  d->PC->Mana+=2;
  }

  next[dim_y] = d->PC->position[dim_y];
  next[dim_x] = d->PC->position[dim_x];
 

  switch (dir) {
  case 1:
  case 2:
  case 3:
    next[dim_y]++;
    break;
  case 4:
  case 5:
  case 6:
    break;
  case 7:
  case 8:
  case 9:
    next[dim_y]--;
    break;
  }
  switch (dir) {
  case 1:
  case 4:
  case 7:
    next[dim_x]--;
    break;
  case 2:
  case 5:
  case 8:
    break;
  case 3:
  case 6:
  case 9:
    next[dim_x]++;
    break;
  case '<':
    if (mappair(d->PC->position) == ter_stairs_up) {
      was_stairs = 1;
      new_dungeon_level(d, '<');
    }
    break;
  case '>':
    if (mappair(d->PC->position) == ter_stairs_down) {
      was_stairs = 1;
      new_dungeon_level(d, '>');
    }
    break;
  }
  int weight=0;
  int h;
    for ( h =0;h<12;h++){
      
      if(d->PC->equip[h]){weight+=d->PC->equip[h]->get_weight();
      }
     
    }
    for ( h =0;h<10;h++){
      if(h==2){
	h++;
      }
      if(d->PC->invt[h]){weight+=d->PC->invt[h]->get_weight();
      }
     
    }
   if(objpair(next)&&weight<d->PC->weight){
     if(objpair(next)->get_type()== objtype_WEAPON&&d->PC->equip[weapon]==NULL){
  d->PC->equip[weapon] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[weapon]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
   else   if(objpair(next)->get_type()== objtype_OFFHAND&&d->PC->equip[offH]==NULL){
  d->PC->equip[offH] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[offH]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_RANGED&&d->PC->equip[range]==NULL){
  d->PC->equip[range] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[range]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_LIGHT&&d->PC->equip[light]==NULL){
  d->PC->equip[light] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[light]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_HELMET&&d->PC->equip[helm]==NULL){
  d->PC->equip[helm] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[helm]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_CLOAK&&d->PC->equip[cloak]==NULL){
  d->PC->equip[cloak] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[cloak]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_GLOVES&&d->PC->equip[gloves]==NULL){
  d->PC->equip[gloves] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[gloves]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_ARMOR&&d->PC->equip[arm]==NULL){
  d->PC->equip[arm] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[arm]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_HELMET&&d->PC->equip[helm]==NULL){
  d->PC->equip[helm] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[helm]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_RING&&d->PC->equip[ring1]==NULL){
  d->PC->equip[ring1] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[ring1]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_RING&&d->PC->equip[ring2]==NULL){
  d->PC->equip[ring2] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[ring2]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else   if(objpair(next)->get_type()== objtype_BOOTS&&d->PC->equip[boots]==NULL){
  d->PC->equip[boots] = objpair(next);
  io_queue_message("GOT ITEM %s,%d",d->PC->equip[boots]->get_name(),d->PC->equipC);
  d->PC->equipC++;
  objpair(next)=NULL;
     }
      else if(d->PC->invC<10){
	 d->PC->invt[d->PC->invC] = objpair(next);
	 io_queue_message("GOT ITEM %s,%d",d->PC->invt[d->PC->invC]->get_name(),d->PC->invC);
  d->PC->invC++;
  objpair(next)=NULL;

      }
  }
   calc_speed(d);
  if (was_stairs) {
    return 0;
  }

  if ((dir != '>') && (dir != '<') && (mappair(next) >= ter_floor)) {
    move_character(d, d->PC, next);
    dijkstra(d);
    dijkstra_tunnel(d);

    return 0;
  } else if (mappair(next) < ter_floor) {
    io_queue_message(wallmsg[rand() % (sizeof (wallmsg) /
                                       sizeof (wallmsg[0]))]);
    io_display(d);
  }

  return 1;
}
