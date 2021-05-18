using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using uDev.Data;
using uDev.Models.Entity;

namespace uDev.Controllers
{
    public class SpecialtyLanguagesController : Controller
    {
        private readonly ApplicationDbContext _context;

        public SpecialtyLanguagesController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: SpecialtyLanguages
       [Authorize(Roles = "Administrator, Customer")]
        public async Task<IActionResult> Index()
        {
            return View(await _context.SpecialtyLanguages.ToListAsync());
        }

        // GET: SpecialtyLanguages/Details/5
        [Authorize(Roles = "Administrator, Customer")]
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var specialtyLanguage = await _context.SpecialtyLanguages
                .FirstOrDefaultAsync(m => m.SpecialtyLanguageId == id);
            if (specialtyLanguage == null)
            {
                return NotFound();
            }

            return View(specialtyLanguage);
        }

        // GET: SpecialtyLanguages/Create
        [Authorize(Roles = "Administrator, Customer")]
        public IActionResult Create()
        {
            return View();
        }

        // POST: SpecialtyLanguages/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = "Administrator, Customer")]
        public async Task<IActionResult> Create([Bind("SpecialtyLanguageId,Name")] SpecialtyLanguage specialtyLanguage)
        {
            if (ModelState.IsValid)
            {
                _context.Add(specialtyLanguage);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(specialtyLanguage);
        }

        // GET: SpecialtyLanguages/Edit/5
        [Authorize(Roles = "Administrator, Customer")]
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var specialtyLanguage = await _context.SpecialtyLanguages.FindAsync(id);
            if (specialtyLanguage == null)
            {
                return NotFound();
            }
            return View(specialtyLanguage);
        }

        // POST: SpecialtyLanguages/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = "Administrator, Customer")]
        public async Task<IActionResult> Edit(int id, [Bind("SpecialtyLanguageId,Name")] SpecialtyLanguage specialtyLanguage)
        {
            if (id != specialtyLanguage.SpecialtyLanguageId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(specialtyLanguage);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!SpecialtyLanguageExists(specialtyLanguage.SpecialtyLanguageId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            return View(specialtyLanguage);
        }

        // GET: SpecialtyLanguages/Delete/5
        [Authorize(Roles = "Administrator, Customer")]
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var specialtyLanguage = await _context.SpecialtyLanguages
                .FirstOrDefaultAsync(m => m.SpecialtyLanguageId == id);
            if (specialtyLanguage == null)
            {
                return NotFound();
            }

            return View(specialtyLanguage);
        }

        // POST: SpecialtyLanguages/Delete/5
        [Authorize(Roles = "Administrator, Customer")]
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var specialtyLanguage = await _context.SpecialtyLanguages.FindAsync(id);
            _context.SpecialtyLanguages.Remove(specialtyLanguage);
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool SpecialtyLanguageExists(int id)
        {
            return _context.SpecialtyLanguages.Any(e => e.SpecialtyLanguageId == id);
        }
    }
}
