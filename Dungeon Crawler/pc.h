#ifndef PC_H
# define PC_H

# include <stdint.h>

# include "dims.h"
# include "character.h"
# include "dungeon.h"
#include "object.h"
class pc : public character {
 public:
  ~pc() {}
  terrain_type known_terrain[DUNGEON_Y][DUNGEON_X];
  uint8_t visible[DUNGEON_Y][DUNGEON_X];
  object* equip[12];
  object* invt[10];
  int invC;
  int equipC;
  int Boss;
  int hit;
  int defence;
  int dodge;
  int weight;
  int Mana;
   int cdH;
    int cdS;
 
};
typedef enum item {
  weapon,offH,range,arm,helm,cloak,gloves,boots,amulet,light,ring1,ring2
} item_t;

void pc_delete(pc *pc);
uint32_t pc_is_alive(dungeon *d);
void config_pc(dungeon *d);
uint32_t pc_next_pos(dungeon *d, pair_t dir);
void place_pc(dungeon *d);
uint32_t pc_in_room(dungeon *d, uint32_t room);
void pc_learn_terrain(pc *p, pair_t pos, terrain_type ter);
terrain_type pc_learned_terrain(pc *p, int16_t y, int16_t x);
void pc_init_known_terrain(pc *p);
void pc_observe_terrain(pc *p, dungeon *d);
int32_t is_illuminated(pc *p, int16_t y, int16_t x);
void pc_reset_visibility(pc *p);

#endif
