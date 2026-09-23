import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ElementRef, ViewChild } from '@angular/core';
import html2canvas from 'html2canvas';
import { jsPDF } from 'jspdf';

interface ResumeData {
	name: string;
	title: string;
	email: string;
	phone: string;
	location: string;
	profile: string;
	experience: string[];
	education: string[];
	skills: string;
}

@Component({
	selector: 'app-editor',
	standalone: true,
	imports: [CommonModule, FormsModule, RouterLink],
	templateUrl: './editor.component.html',
	 styles: ['.paper.clean{font-family:Arial,sans-serif}.paper.clean h1{font-family:Arial,sans-serif}.paper.bold{border-left:8px solid #202a25}.paper.mono{font-family:monospace}.paper.soft{background:#f0f1eb}.paper.frame{border:8px solid #eee}.paper.meridian,.paper.ledger{font-family:Arial,sans-serif}.paper.meridian h1,.paper.ledger h1{font-family:Arial,sans-serif;letter-spacing:.5px}.paper.meridian .preview-photo,.paper.ledger .preview-photo,.paper.split-light .preview-photo,.paper.split-slate .preview-photo{display:none}.paper.split-light,.paper.split-slate{padding-left:30%;min-height:700px;border-left:0;position:relative}.paper.split-light{background:linear-gradient(90deg,#cfe8ff 0 27%,#f5faff 27%)}.paper.split-slate{background:linear-gradient(90deg,#f6d5e1 0 27%,#fff5f8 27%)}.paper.split-light h1,.paper.split-slate h1{font-family:Arial,sans-serif;font-size:25px}.paper.meridian,.paper.ledger{border-top:7px solid #202a25}.preview-photo-circle{width:65px;height:65px;border-radius:50%;background-color:#dedbd2;background-size:cover;background-position:center;background-repeat:no-repeat;display:grid;place-items:center;font-weight:700;overflow:hidden}.repeat .text-editor{flex:1;min-width:0}.repeat .text-editor textarea{min-height:105px}.editor-actions{display:flex;align-items:center;gap:10px}.format-picker{display:flex;align-items:center;gap:6px;color:#68716a;font-size:10px;font-weight:700;white-space:nowrap}.format-picker select{padding:8px 9px;border:1px solid #dfe2de;border-radius:7px;background:#fff;color:#1d2420}']
})
export class EditorComponent {
	private readonly apiUrl = 'http://localhost:8080/api/resumes';
	private readonly ownerEmail: string;
	private resumeId?: number;
	templateStyle = 'serif';
	saveStatus = 'Not saved yet';
	downloadFormat: 'html' | 'pdf' | 'jpg' | 'jpeg' = 'pdf';
	@ViewChild('resumePreview') resumePreview?: ElementRef<HTMLElement>;

	resume: ResumeData = {
		name: 'Alex Morgan',
		title: 'Product Engineer',
		email: 'alex@example.com',
		phone: '+94 77 123 4567',
		location: 'Colombo, Sri Lanka',
		profile: 'Creative professional with expertise in product engineering and design.',
		experience: ['Senior Software Engineer · Bright Labs · 2024 — Present'],
		education: ['BSc (Hons) Information Technology · University of Moratuwa'],
		skills: 'Angular · Java · Spring Boot · MongoDB · Git'
	};

	photo = 'AM';

	constructor(private route: ActivatedRoute, private http: HttpClient) {
		const session = JSON.parse(localStorage.getItem('foliofield-session') || '{}');
		this.ownerEmail = session.email || '';
		const selectedTemplate = this.route.snapshot.queryParamMap.get('template');
		if (selectedTemplate) this.templateStyle = selectedTemplate;
		const savedResumeId = this.route.snapshot.queryParamMap.get('resumeId');
		if (savedResumeId && this.ownerEmail) {
			this.http.get<any>(`${this.apiUrl}/${savedResumeId}?ownerEmail=${encodeURIComponent(this.ownerEmail)}`).subscribe({
				next: saved => this.loadSavedResume(saved),
				error: () => this.saveStatus = 'Could not load'
			});
		} else {
			const savedResume = localStorage.getItem('foliofield-resume');
			if (savedResume) this.resume = JSON.parse(savedResume);
		}
	}

	private loadSavedResume(saved: any) {
		this.resumeId = saved.id;
		this.templateStyle = saved.template || this.templateStyle;
		this.resume = {
			name: saved.name || '',
			title: saved.targetRole || '',
			email: saved.email || '',
			phone: saved.phone || '',
			location: saved.location || '',
			profile: saved.summary || '',
			experience: saved.experience ? saved.experience.split('\n') : [''],
			education: saved.education ? saved.education.split('\n') : [''],
			skills: saved.skills || ''
		};
		this.saveStatus = 'Saved locally';
	}

	noPhotoTemplate() {
		return ['mono', 'meridian', 'ledger', 'split-light', 'split-slate'].includes(this.templateStyle);
	}

	saveResume() {
		localStorage.setItem('foliofield-resume', JSON.stringify(this.resume));
		if (!this.ownerEmail) {
			this.saveStatus = 'Sign in to save';
			return;
		}
		const request = this.toResumeRequest();
		const saveRequest = this.resumeId
			? this.http.put(`${this.apiUrl}/${this.resumeId}?ownerEmail=${encodeURIComponent(this.ownerEmail)}`, request)
			: this.http.post<{id: number}>(`${this.apiUrl}?ownerEmail=${encodeURIComponent(this.ownerEmail)}`, request);
		saveRequest.subscribe({
			next: saved => {
				if (!this.resumeId) this.resumeId = (saved as {id: number}).id;
				localStorage.setItem(`foliofield-resume-id:${this.ownerEmail}`, String(this.resumeId));
				this.saveStatus = 'Saved just now';
			},
			error: () => this.saveStatus = 'Could not save'
		});
	}

	private toResumeRequest() {
		return {name: this.resume.name, targetRole: this.resume.title, location: this.resume.location, email: this.resume.email, summary: this.resume.profile, experience: this.resume.experience.join('\n'), education: this.resume.education.join('\n'), skills: this.resume.skills, template: this.templateStyle, tone: 'Professional', accent: 'Slate'};
	}

	async downloadResume() {
		if (this.downloadFormat === 'html') {
			this.downloadHtml();
			return;
		}

		if (!this.resumePreview) return;
		const canvas = await html2canvas(this.resumePreview.nativeElement, {
			backgroundColor: '#ffffff',
			scale: 2,
			useCORS: true
		});
		const baseName = this.resume.name.replace(/[^a-z0-9]+/gi, '-').toLowerCase() || 'resume';

		if (this.downloadFormat === 'pdf') {
			const pdf = new jsPDF({ orientation: 'portrait', unit: 'px', format: 'a4' });
			const pageWidth = pdf.internal.pageSize.getWidth();
			const pageHeight = (canvas.height * pageWidth) / canvas.width;
			pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 0, 0, pageWidth, pageHeight);
			pdf.save(`${baseName}.pdf`);
			return;
		}

		const mimeType = this.downloadFormat === 'jpg' ? 'image/jpeg' : 'image/jpeg';
		const link = document.createElement('a');
		link.href = canvas.toDataURL(mimeType, 0.92);
		link.download = `${baseName}.${this.downloadFormat}`;
		link.click();
	}

	private downloadHtml() {
		const photo = this.photo.startsWith('data:')
			? `<img class="photo" src="${this.photo}" alt="Profile photo">`
			: `<div class="photo initials">${this.photo}</div>`;
		const content = `<!doctype html><html><head><meta charset="utf-8"><title>${this.resume.name} - CV</title><style>
			body{margin:0;background:#eee;color:#1d2420;font-family:Arial,sans-serif}.paper{box-sizing:border-box;width:800px;min-height:1100px;margin:30px auto;padding:70px;background:#fff;border-left:8px solid #202a25}.photo{width:70px;height:70px;border-radius:50%;object-fit:cover}.initials{display:grid;place-items:center;background:#dedbd2;font-weight:bold}.paper h1{font:600 38px Georgia,serif;margin:24px 0 6px}.title{color:#66716b}.contact{color:#66716b;font-size:13px}.paper hr{border:0;border-top:1px solid #ddd;margin:26px 0}.section{margin-top:24px}.section h2{font-size:11px;letter-spacing:2px}.section p{font-size:14px;line-height:1.6}@media print{body{background:#fff}.paper{margin:0;box-shadow:none}}
			</style></head><body><main class="paper">${photo}<h1>${this.escapeHtml(this.resume.name)}</h1><p class="title">${this.escapeHtml(this.resume.title)}</p><p class="contact">${this.escapeHtml(this.resume.location)} · ${this.escapeHtml(this.resume.email)} · ${this.escapeHtml(this.resume.phone)}</p><hr><section class="section"><h2>PROFILE</h2><p>${this.formatText(this.resume.profile)}</p></section><section class="section"><h2>EXPERIENCE</h2>${this.resume.experience.map(item => `<p>${this.formatText(item)}</p>`).join('')}</section><section class="section"><h2>EDUCATION</h2>${this.resume.education.map(item => `<p>${this.formatText(item)}</p>`).join('')}</section><section class="section"><h2>SKILLS</h2><p>${this.formatText(this.resume.skills)}</p></section></main></body></html>`;
		const file = new Blob([content], { type: 'text/html' });
		const url = URL.createObjectURL(file);
		const link = document.createElement('a');
		link.href = url;
		link.download = `${this.resume.name.replace(/[^a-z0-9]+/gi, '-').toLowerCase() || 'resume'}.html`;
		link.click();
		URL.revokeObjectURL(url);
	}

	private escapeHtml(value: string) {
		return value.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#039;');
	}

	addExperience() {
		this.resume.experience.push('');
	}

	removeExperience(index: number) {
		if (this.resume.experience.length > 1) this.resume.experience.splice(index, 1);
	}

	addEducation() {
		this.resume.education.push('');
	}

	removeEducation(index: number) {
		if (this.resume.education.length > 1) this.resume.education.splice(index, 1);
	}

	trackByIndex(index: number) {
		return index;
	}

	formatText(value: string) {
		const escaped = value
			.replace(/&/g, '&amp;')
			.replace(/</g, '&lt;')
			.replace(/>/g, '&gt;')
			.replace(/"/g, '&quot;')
			.replace(/'/g, '&#039;');

		return escaped
			.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
			.replace(/_(.+?)_/g, '<em>$1</em>')
			.replace(/^•\s?(.*)$/gm, '&bull; $1')
			.replace(/\n/g, '<br>');
	}

	onPhotoUpload(event: Event) {
		const input = event.target as HTMLInputElement;
		const file = input.files?.[0];
		if (!file) return;

		const reader = new FileReader();
		reader.onload = () => this.photo = String(reader.result);
		reader.readAsDataURL(file);
	}

	applyFormat(format: 'bold' | 'italic' | 'list', textarea: HTMLTextAreaElement) {
		const start = textarea.selectionStart;
		const end = textarea.selectionEnd;
		const selectedText = textarea.value.slice(start, end) || 'text';
		const formattedText = format === 'bold'
			? `**${selectedText}**`
			: format === 'italic'
				? `_${selectedText}_`
				: `• ${selectedText}`;

		textarea.setRangeText(formattedText, start, end, 'select');
		textarea.dispatchEvent(new Event('input', { bubbles: true }));
		textarea.focus();
	}
}