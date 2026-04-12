using System;
using System.Collections.Generic;

namespace IMPULS_Desktop
{
    internal class Empresa
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public string Email { get; set; }

        public string Address { get; set; }

        public string VatNumber { get; set; }

        public string Website { get; set; }

        public string Phone { get; set; }

        public string Niche { get; set; }

        public string Technologies { get; set; }

        public string ProfilePhoto { get; set; }

        public string Username { get; set; }

        public string Password { get; set; }
        
        public List<Oferta> ActiveOffers { get; set; } = new List<Oferta>();
    }
}